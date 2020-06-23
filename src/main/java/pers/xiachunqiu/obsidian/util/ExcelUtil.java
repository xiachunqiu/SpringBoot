package pers.xiachunqiu.obsidian.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExcelUtil {
    private ExcelUtil() {
    }

    public static void main(String[] args) {
        // 子表目录路径
        String filePathToBeRead = "C:\\Users\\夏春秋\\Desktop\\sub";
        // 总表文件路径
        String fileToBeWrite = "C:\\Users\\夏春秋\\Desktop\\general\\2020年3月上半月.xls";
        System.out.println(">>> start");
        File file = new File(filePathToBeRead);
        String[] fileNames = file.list();
        assert fileNames != null;
        for (String fileName : fileNames) {
            String fileToBeRead = filePathToBeRead + "\\" + fileName;
            Map<String, String> startTimeMap = Maps.newHashMap();
            Map<String, String> endTimeMap = Maps.newHashMap();
            int startTimeColumnIndex = -1;
            int endTimeColumnIndex = -1;
            int dateColumnIndex = -1;
            String[] keyArray = new String[]{"负责人", "施工地点", "设备", "工作内容"};
            List<Integer> keyIndexList = Lists.newArrayList();
            boolean flag = true;
            try {
                Workbook workbook = readExcel(fileToBeRead);
                assert workbook != null;
                String sheetName = workbook.getSheetName(0);
                Sheet sheet = workbook.getSheet(sheetName);
                for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                    if (flag) {
                        Row row = sheet.getRow(i);
                        if (startTimeColumnIndex == -1 && endTimeColumnIndex == -1 && dateColumnIndex == -1) {
                            for (int j = 0; j <= 50; j++) {
                                Cell cell = row.getCell(j);
                                if (cell != null) {
                                    String stringCellValue = cell.getStringCellValue();
                                    if ("实际开始时间".equals(stringCellValue)) {
                                        startTimeColumnIndex = j;
                                    }
                                    if ("实际结束时间".equals(stringCellValue)) {
                                        endTimeColumnIndex = j;
                                    }
                                    if ("施工日期".equals(stringCellValue)) {
                                        dateColumnIndex = j;
                                    }
                                    for (String s : keyArray) {
                                        if (s.equals(stringCellValue)) {
                                            keyIndexList.add(j);
                                        }
                                    }
                                }
                            }
                        } else {
                            if (keyIndexList.size() != keyArray.length) {
                                System.out.println(fileName + "中以下列存在一项或多项缺失：[负责人], [施工地点], [设备], [工作内容]，需要手动复制到总表");
                                flag = false;
                                continue;
                            }
                            try {
                                Cell startTimeCell = row.getCell(startTimeColumnIndex);
                                Cell endTimeCell = row.getCell(endTimeColumnIndex);
                                Cell dateCell = row.getCell(dateColumnIndex);
                                String startTimeString;
                                String endTimeString;
                                if (startTimeCell != null && endTimeCell != null && dateCell != null) {
                                    startTimeString = getDateString(startTimeCell);
                                    if (startTimeString == null) continue;
                                    endTimeString = getDateString(endTimeCell);
                                    if (endTimeString == null) continue;
                                    Date date = dateCell.getDateCellValue();
                                    if (date == null) continue;
                                    String key = assembleMapKey(keyIndexList, row, date);
                                    String s = startTimeMap.get(key);
                                    if (s != null) {
                                        System.out.println(key);
                                        System.out.println("文件" + fileName + ">>>第" + (i + 1) + "行重复");
                                    }
                                    startTimeMap.put(key, startTimeString);
                                    endTimeMap.put(key, endTimeString);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(fileName + ">>>fail>>>第" + (i + 1) + "行>>>" + e.getMessage());
                            }
                        }
                    }
                }
                if (flag) {
                    Workbook workbookWrite = readExcel(fileToBeWrite);
                    assert workbookWrite != null;
                    Sheet sheetWrite = workbookWrite.getSheet(sheetName);
                    if (sheetWrite != null) {
                        int startTimeColumnIndexForWrite = -1;
                        int endTimeColumnIndexForWrite = -1;
                        int dateColumnIndexForWrite = -1;
                        List<Integer> keyIndexListForWrite = Lists.newArrayList();
                        for (int i = 0; i <= sheetWrite.getLastRowNum(); i++) {
                            Row row = sheetWrite.getRow(i);
                            if (startTimeColumnIndexForWrite == -1 && endTimeColumnIndexForWrite == -1 && dateColumnIndexForWrite == -1) {
                                for (int j = 0; j <= sheetWrite.getLastRowNum(); j++) {
                                    Cell cell = row.getCell(j);
                                    if (cell != null) {
                                        String stringCellValue = cell.getStringCellValue();
                                        for (String s : keyArray) {
                                            if (s.equals(stringCellValue)) {
                                                keyIndexListForWrite.add(j);
                                            }
                                        }
                                        if ("实际开始时间".equals(stringCellValue)) {
                                            startTimeColumnIndexForWrite = j;
                                        }
                                        if ("实际结束时间".equals(stringCellValue)) {
                                            endTimeColumnIndexForWrite = j;
                                        }
                                        if ("施工日期".equals(stringCellValue)) {
                                            dateColumnIndexForWrite = j;
                                        }
                                    }
                                }
                            } else {
                                Cell dateCell = row.getCell(dateColumnIndex);
                                if (dateCell != null) {
                                    Date date = dateCell.getDateCellValue();
                                    if (date != null) {
                                        String key = assembleMapKey(keyIndexListForWrite, row, date);
                                        String startTimeString = startTimeMap.get(key);
                                        String endTimeString = endTimeMap.get(key);
                                        if (startTimeString != null && endTimeString != null) {
                                            Cell startTimeCell = row.getCell(startTimeColumnIndex);
                                            Cell endTimeCell = row.getCell(endTimeColumnIndex);
                                            startTimeCell.setCellValue(startTimeString);
                                            endTimeCell.setCellValue(endTimeString);
                                        }
                                    }
                                }
                            }
                        }
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(fileToBeWrite);
                            workbookWrite.write(out);
                            System.out.println(fileName + ">>>success");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                assert out != null;
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        System.out.println(fileName + "中的此Sheet页:" + sheetName + ">>>在" + fileToBeWrite + "中不存在");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(fileName + ">>>fail");
            }
        }
    }

    private static String getDateString(Cell startTimeCell) {
        String startTimeString;
        try {
            Date startTime = startTimeCell.getDateCellValue();
            if (startTime == null) {
                return null;
            }
            startTimeString = format("HH:mm", startTime);
        } catch (Exception e) {
            startTimeString = startTimeCell.getStringCellValue();
            if (isNull(startTimeString)) {
                return null;
            }
        }
        return startTimeString;
    }

    private static String assembleMapKey(List<Integer> keyIndexList, Row row, Date date) {
        StringBuilder key = new StringBuilder();
        String dateString = format("MM月dd日", date);
        key.append(dateString);
        keyIndexList.forEach(keyIndex -> {
            Cell cell = row.getCell(keyIndex);
            if (cell != null) {
                String cellStringValue = cell.getStringCellValue();
                if (cellStringValue != null) {
                    key.append(cellStringValue);
                }
            }
        });
        return key.toString();
    }

    private static String format(String pattern, Date date) {
        return date == null ? "" : (new SimpleDateFormat(pattern)).format(date);
    }

    private static boolean isNull(String checkStr) {
        return checkStr == null || checkStr.trim().length() == 0 || "null".equalsIgnoreCase(checkStr.trim());
    }

    private static Workbook readExcel(String fileName) {
        if (fileName == null) {
            return null;
        }
        String extString = fileName.substring(fileName.lastIndexOf("."));
        try {
            InputStream is = new FileInputStream(fileName);
            if (".xls".equals(extString)) {
                return new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                return new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}