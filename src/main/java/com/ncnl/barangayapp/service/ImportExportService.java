package com.ncnl.barangayapp.service;

import com.ncnl.barangayapp.model.Resident;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class ImportExportService {

    private static final ResidentService residentService = new ResidentService();
    private static final List<String> supportedDatePatterns = List.of(
            "dd/MM/yyyy", "MM/dd/yyyy", "d-MMM-yy", "dd-MMM-yyyy"
    );

    public static void handleDataImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Resident Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls"));

        File file = fileChooser.showOpenDialog(null);
        if (file == null) return;

        StageService.getInstance().showLoading("Importing residents...");

        Task<Void> importTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try (FileInputStream fis = new FileInputStream(file); Workbook workbook = getWorkbook(file, fis)) {
                    for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                        Sheet sheet = workbook.getSheetAt(i);

                        for (Row row : sheet) {
                            if (row.getRowNum() == 0) continue; // Skip headers

                            try {
                                Resident resident = Resident.builder()
                                        .id(UUID.randomUUID())
                                        .lastname(getCellString(row, 0))
                                        .firstname(getCellString(row, 1))
                                        .middlename(getCellString(row, 2))
                                        .qualifier(getCellString(row, 3))
                                        .number(getCellString(row, 4)) // Handle "0793"
                                        .streetName(getCellString(row, 5))
                                        .location(getCellString(row, 6))
                                        .placeOfBirth(getCellString(row, 7))
                                        .dateOfBirth(getCellString(row, 8))
                                        .age(calculateAge(getCellString(row, 8)))
                                        .sex(getCellString(row, 9))
                                        .civilStatus(getCellString(row, 10))
                                        .citizenship(getCellString(row, 11))
                                        .occupation(getCellString(row, 12))
                                        .relationshipToHouseHoldHead(getCellString(row, 13))
                                        .status("Unclaimed")
                                        .build();

                                if (residentService.exists(resident.getId())) {
                                    // Optional: handle duplicates
                                    continue;
                                }

                                residentService.addImportedResident(resident);

                            } catch (Exception e) {
                                e.printStackTrace(); // Handle bad rows gracefully
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Import failed: " + e.getMessage());
                }

                return null;
            }

            @Override
            protected void succeeded() {
                StageService.getInstance().hideLoading();
                AlertService.getInstance().showInfo("Import Complete", "Residents imported successfully.");
            }

            @Override
            protected void failed() {
                StageService.getInstance().hideLoading();
                AlertService.getInstance().showWarning("Import Failed", "Error occurred during import.");
            }
        };

        new Thread(importTask).start();
    }

    private static Workbook getWorkbook(File file, FileInputStream fis) throws Exception {
        if (file.getName().endsWith(".xlsx")) return new XSSFWorkbook(fis);
        if (file.getName().endsWith(".xls")) return new HSSFWorkbook(fis);
        throw new IllegalArgumentException("Unsupported file type: " + file.getName());
    }

    private static String getCellString(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell == null || cell.getCellType() == CellType.BLANK) return "N/A";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue());
                } else {
                    double val = cell.getNumericCellValue();
                    if (val == Math.floor(val)) {
                        return String.format("%04d", (int) val); // For things like "0793"
                    }
                    return String.valueOf(val);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (IllegalStateException e) {
                    try {
                        return new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue());
                    } catch (Exception ex) {
                        return String.valueOf((int) cell.getNumericCellValue());
                    }
                }
            default:
                return "N/A";
        }
    }

    private static int calculateAge(String dobString) {
        for (String pattern : supportedDatePatterns) {
            try {
                Date date = new SimpleDateFormat(pattern, Locale.ENGLISH).parse(dobString.trim());
                LocalDate birthDate = new java.sql.Date(date.getTime()).toLocalDate();
                return Period.between(birthDate, LocalDate.now()).getYears();
            } catch (Exception ignored) {
            }
        }
        return 0;
    }

    public static void handleDataExport(Iterable<Resident> residentList) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Resident Data");
        fileChooser.setInitialFileName("RESIDENT_DATA_EXPORT_" + LocalDate.now() + ".xlsx");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        File file = fileChooser.showSaveDialog(null);
        if (file == null) return;

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Residents");

            Row header = sheet.createRow(0);
            String[] headers = {"ID", "Lastname", "Firstname", "Middlename", "Qualifier", "Number", "Street Name", "Location", "Place of Birth", "Date of Birth", "Age", "Sex", "Civil Status", "Citizenship", "Occupation", "Relationship to Household Head", "Status", "Timestamp"};
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (Resident r : residentList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(r.getId().toString());
                row.createCell(1).setCellValue(r.getLastname());
                row.createCell(2).setCellValue(r.getFirstname());
                row.createCell(3).setCellValue(r.getMiddlename());
                row.createCell(4).setCellValue(r.getQualifier());
                row.createCell(5).setCellValue(r.getNumber());
                row.createCell(6).setCellValue(r.getStreetName());
                row.createCell(7).setCellValue(r.getLocation());
                row.createCell(8).setCellValue(r.getPlaceOfBirth());
                row.createCell(9).setCellValue(r.getDateOfBirth());
                row.createCell(10).setCellValue(r.getAge());
                row.createCell(11).setCellValue(r.getSex());
                row.createCell(12).setCellValue(r.getCivilStatus());
                row.createCell(13).setCellValue(r.getCitizenship());
                row.createCell(14).setCellValue(r.getOccupation());
                row.createCell(15).setCellValue(r.getRelationshipToHouseHoldHead());
                row.createCell(16).setCellValue(r.getStatus());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }

            AlertService.getInstance().showInfo("Export Successful", "Resident data has been exported.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertService.getInstance().showWarning("Export Failed", "An error occurred while exporting data.");
        }
    }

}
