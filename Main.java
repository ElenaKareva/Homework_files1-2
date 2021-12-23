import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        String pathDirectory = "\\Games1";

        //Создание директорий src, res, saveGames, temp
        File src = createDirectory("src", pathDirectory, sb);
        File res = createDirectory("res", pathDirectory, sb);
        File saveGames = createDirectory("saveGames", pathDirectory, sb);
        File temp = createDirectory("temp", pathDirectory, sb);

        //создание директорий main, test в src
        File main = createDirectory("main", src.getAbsolutePath(), sb);
        File test = createDirectory("test", src.getAbsolutePath(), sb);

        //создание Main.java, Utils.java в Main
        createFile("Main.java", main.getAbsolutePath(), sb);
        createFile("Utils.java", main.getAbsolutePath(), sb);

        //создание директорий drawables, vectors, icons в res
        createDirectory("drawables", res.getAbsolutePath(), sb);
        createDirectory("vectors", res.getAbsolutePath(), sb);
        createDirectory("icons", res.getAbsolutePath(), sb);

        //создание файла temp.txt в директории temp
        File tempTxt = createFile("temp.txt", temp.getAbsolutePath(), sb);


        //   запись в файл temp.txt данных о создании папок и файлов
        try (FileWriter fw = new FileWriter(tempTxt.getAbsolutePath())) {
            fw.write(sb.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(sb.toString());

        //Задание 2
        GameProgress gameProgress1 = new GameProgress(100, 10, 36, 100);
        GameProgress gameProgress2 = new GameProgress(50, 5, 18, 50);
        GameProgress gameProgress3 = new GameProgress(25, 2, 9, 25);

        saveGame("\\Games1\\saveGames\\save1.dat", gameProgress1);
        saveGame("\\Games1\\saveGames\\save2.dat", gameProgress2);
        saveGame("\\Games1\\saveGames\\save3.dat", gameProgress3);

        List<String> paths = new ArrayList<>();
        paths.add("\\Games1\\saveGames\\save1.dat");
        paths.add("\\Games1\\saveGames\\save2.dat");
        paths.add("\\Games1\\saveGames\\save3.dat");

        zipFiles("\\Games1\\saveGames\\save.zip", paths);
        deleteUnpackingFiles();
    }

    //создание директории
    public static File createDirectory(String directoryName, String pathDirectory, StringBuilder sb) {
        String newPath = pathDirectory + "\\" + directoryName;
        File file = new File(newPath);
        if (file.mkdir()) {
            sb.append("Директория ")
                    .append(file.getName())
                    .append(" успешно создана")
                    .append("\n");
        } else {
            sb.append("Не удалось создать Директорию ")
                    .append(file.getName())
                    .append("\n");
        }
        return file;
    }


    // создание файла
    public static File createFile(String fileName, String pathDirectory, StringBuilder sb) {
        String newPath = pathDirectory + "\\" + fileName;
        File file = new File(newPath);
        try {
            if (file.createNewFile()) {
                sb.append("Файл ")
                        .append(file.getName())
                        .append(" успешно создан\n");
            } else {
                sb.append("Не удалось создать файл ")
                        .append(file.getName())
                        .append("\n");
            }
        } catch (IOException e) {
            sb.append("Проблема\n");
        }
        return file;
    }


    //сохранение
    private static void saveGame(String filePath, GameProgress instance) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(instance);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //архивация
    private static void zipFiles(String filePath, List<String> listObjectPath) {
        try (ZipOutputStream zous = new ZipOutputStream(new FileOutputStream(filePath))) {
            int count = 1;
            for (String path : listObjectPath) {
                FileInputStream fis = new FileInputStream(path);
                ZipEntry entry = new ZipEntry("packed_save" + count++ + ".dat");
                zous.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zous.write(buffer);
                zous.closeEntry();
                fis.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //удаление файлов сохранений, лежащих вне архива
    private static void deleteUnpackingFiles() {
        File files = new File("\\Games1\\saveGames");
        if (files.isDirectory()) {
            for (File file : files.listFiles()) {
                if (!file.getName().contains(".zip")) {
                    file.delete();
                }
            }
        }
    }
}