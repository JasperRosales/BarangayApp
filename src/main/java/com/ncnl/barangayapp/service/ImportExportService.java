package com.ncnl.barangayapp.service;

import com.ncnl.barangayapp.model.Resident;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ImportExportService {

    private static final ResidentService residentService = new ResidentService();

    public static void handleDataImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Resident Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        File file = fileChooser.showOpenDialog(null);
        if (file == null) return;

        try (FileInputStream fis = new FileInputStream(file); XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                String id = getCellString(row, 0);
                String fullname = getCellString(row, 1);
                String sex = getCellString(row, 2);
                int age = (int) getCellNumeric(row, 3);
                String location = getCellString(row, 4);
                String category = getCellString(row, 5);
                String info = getCellString(row, 6);
                String status = getCellString(row, 7);
                String timestamp = getCellString(row, 8);

                Resident resident = Resident.builder()
                        .id((id == null || id.isBlank()) ? UUID.randomUUID() : UUID.fromString(id))
                        .fullname(fullname)
                        .sex(sex)
                        .age(age)
                        .location(location)
                        .category(category)
                        .additional_info(info)
                        .status(status)
                        .timestamp((timestamp == null || timestamp.isBlank()) ? LocalDateTime.now().toString() : timestamp)
                        .build();

                UUID residentId = resident.getId();
                if (residentService.exists(residentId)) {
                    String decision = AlertService.getInstance().showChoice(
                            "Duplicate Resident Found",
                            "Resident with ID " + residentId + " already exists.\nWhat would you like to do?",
                            List.of("Ignore", "Overwrite")
                    );

                    if (decision.equals("Ignore")) continue;
                    if (decision.equals("Overwrite")) {
                        residentService.updateResident(resident);
                    }
                } else {
                    residentService.addImportedResident(resident);
                }
            }

            AlertService.getInstance().showInfo("Import Successful", "Residents imported successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertService.getInstance().showWarning("Import Failed", "There was an error importing the file.");
        }
    }

    public static void handleDataExport(Iterable<Resident> residentList) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Resident Data");
        String fileName = "RESIDENT_DATA_EXPORT_" + LocalDate.now() + ".xlsx";
        fileChooser.setInitialFileName(fileName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        File file = fileChooser.showSaveDialog(null);
        if (file == null) return;

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Residents");

            Row header = sheet.createRow(0);
            String[] headers = {"ID", "Fullname", "Sex", "Age", "Location", "Category", "Additional Info", "Status", "Timestamp"};
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (Resident r : residentList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(r.getId() != null ? r.getId().toString() : "");
                row.createCell(1).setCellValue(r.getFullname());
                row.createCell(2).setCellValue(r.getSex());
                row.createCell(3).setCellValue(r.getAge());
                row.createCell(4).setCellValue(r.getLocation());
                row.createCell(5).setCellValue(r.getCategory());
                row.createCell(6).setCellValue(r.getAdditional_info());
                row.createCell(7).setCellValue(r.getStatus());
                row.createCell(8).setCellValue(r.getTimestamp());
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

    private static String getCellString(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return cell.getCellType() == CellType.NUMERIC
                ? String.valueOf((int) cell.getNumericCellValue())
                : cell.getStringCellValue().trim();
    }

    private static double getCellNumeric(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell.getCellType() == CellType.STRING) {
            return Double.parseDouble(cell.getStringCellValue());
        }
        return cell.getNumericCellValue();
    }
}
