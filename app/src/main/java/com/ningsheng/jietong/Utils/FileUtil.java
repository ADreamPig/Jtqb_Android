package com.ningsheng.jietong.Utils;

/**
 * Created by zhangheng on 2015/12/2.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileUtil {
    /**
     * 未知操作,只能通过调用方法判断是要操控文件还是目录
     */
    public static final int UNKNOWN = -0x0003;
    /**
     * 无意义的操作
     */
    public static final int NONFEASANCE = -0x0002;
    /**
     * 检测失败
     */
    public static final int CHECK_FAIL = -0x0001;
    /**
     * 文件存在
     */
    public static final int FILE_EXIST_TRUE = 0x0000;
    /**
     * 文件不存在
     */
    public static final int FILE_EXIST_FALSE = 0x0001;
    /**
     * 路径存在
     */
    public static final int FILEPATH_EXIST_TRUE = 0x0002;
    /**
     * 路径不存在
     */
    public static final int FILEPATH_EXIST_FALSE = 0x0004;
    /**
     * 路径存在,文件不存在 创建会失败,这个路径有个目录名和文件名相同[先有路径]
     */
    private static final int FILE_FAIL_FILEpATH_TRUE = FILE_EXIST_FALSE
            + FILEPATH_EXIST_TRUE;// 0x0003
    /**
     * 路径不存在,文件不存在,这种情况是有个文件名和路径名相同[先有文件]
     */
    private static final int FILE_FALSE_FLIEPATH_FAIL = FILE_EXIST_FALSE
            + FILEPATH_EXIST_FALSE;// 0x0005

    /**
     * 构造函数私有化,防止外部创建该类的实例
     */
    private FileUtil() {

    }

    /**
     * 创建文件,调用此方法前先调用checkFile()方法检查File
     *
     * @param path   路径
     * @param name   名称
     * @param result checkFile() 结果码
     * @return 文件创建成功或文件已存在 返回 File,否则返回null
     */
    private static File createFile(String path, String name, int result) {
        File file = new File(path);
        File file_ = null;
        switch (result) {
            case UNKNOWN:

                break;
            case CHECK_FAIL:
                return null;
            case NONFEASANCE:
            case FILEPATH_EXIST_FALSE:
                if (!file.mkdirs()) {
                    return null;
                }
            case FILEPATH_EXIST_TRUE:

            case FILE_EXIST_FALSE:
                file_ = new File(file, name);
                try {
                    if (!file_.createNewFile()) {
                        return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            case FILE_EXIST_TRUE:
                return new File(path, name);
            case FILE_FAIL_FILEpATH_TRUE:
                return null;
            case FILE_FALSE_FLIEPATH_FAIL:
                return null;
        }
        return null;
    }

    /**
     * 创建文件
     *
     * @param mFile
     * @param result
     * @return
     */
    private static File createFile(File mFile, int result) {
        return createFile(mFile.getParent(), mFile.getName(), result);

    }

    /**
     * 创建目录
     *
     * @param path
     * @param result
     * @return
     */
    private static File createDirectory(String path, int result) {
        if (!check(path)) {
            return null;
        }
        return createDirectory(new File(path), result);

    }

    /**
     * 创建目录
     *
     * @param path
     * @return
     */
    private static File createDirectory(String path) {
        if (!check(path)) {
            return null;
        }
        File file = new File(path);
        int result = checkFile(file);
        return createDirectory(new File(path), result);

    }

    /**
     * 创建目录,调用前先调用checkFile()检测File
     *
     * @param mFile
     * @param result checkFile()
     * @return 创建目录成功或目录存在返回File, 否则返回null
     */
    private static File createDirectory(File mFile, int result) {
        switch (result) {
            case UNKNOWN:
                if (!mFile.mkdirs()) {
                    return null;
                } else {
                    return mFile;
                }
            case CHECK_FAIL:
                return null;
            case NONFEASANCE:
            case FILEPATH_EXIST_FALSE:
                if (!mFile.mkdirs()) {
                    return null;
                } else {
                    return mFile;
                }
            case FILEPATH_EXIST_TRUE:
                break;
            case FILE_EXIST_FALSE:
                if (!mFile.mkdirs()) {
                    return null;
                } else {
                    return mFile;
                }
            case FILE_EXIST_TRUE:
                return null;
            case FILE_FAIL_FILEpATH_TRUE:
                return mFile;
            case FILE_FALSE_FLIEPATH_FAIL:
                return null;
        }
        return mFile;

    }

    /**
     * 创建文件
     *
     * @param path
     * @param name
     * @return
     */
    public static File createFile(String path, String name) {
        if (!check(path, name)) {
            return null;
        }
        return createFile(new File(path, name));
    }

    /**
     * 创建文件
     *
     * @param mFile
     * @return 如果mFile为null, 或者路径不正确, 试图创建文件
     */
    public static File createFile(File mFile) {
        if (mFile == null) {
            return null;
        }
        int result = checkFile(mFile);
        if (result == UNKNOWN) {
            try {
                if (mFile.createNewFile())
                    return mFile;
                else
                    return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return createFile(mFile.getParent(), mFile.getName(), result);
    }


    /**
     * 创建文件夹到SD卡根目录下
     *
     * @param dirName 文件夹名字 栗子: /dirname
     * @return
     */
    public static File createFileDir(String dirName) {
        if (existSdcard()) {
            File dir = new File(getSdPath() + dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir;
        }
        return null;
    }

    /**
     * 获取SD卡的path
     *
     * @return
     * @author M.c
     * @since 2014-11-09
     */
    public static String getSdPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory(); // 获取根目录
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    }

    /**
     * 判断sd卡是否安装
     *
     * @return
     * @author M.c
     */
    public static boolean existSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 写入指定路径的文件,默认转为UTF-8写入 [路径不存在自动创建,文件不存在自动创建]
     *
     * @param path
     * @param name
     * @param content
     * @param isAppend 是否追加
     * @return
     */
    public static boolean writeFile(String path, String name, String content,
                                    boolean isAppend) {
        if (!check(path, name, content))
            return false;
        int result = checkFile(path, name);
        File mFile = createFile(path, name, result);
        if (mFile != null) {
            if (!mFile.canWrite()) {
                return false;
            }
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(mFile, isAppend);
                out.write(content.getBytes("UTF-8"));
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null)
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }

        return false;
    }

    /**
     * 从流中读取,写入指定路径的文件中[路径不存在自动创建,文件不存在自动创建]
     *
     * @param path
     * @param name
     * @param input
     * @return 写入成功返回true, 否则返回false
     */
    public static boolean writeFile(String path, String name, InputStream input) {
        if (!check(path, name) || input == null) {
            return false;
        }
        int result = checkFile(path, name);
        File mFile = createFile(path, name, result);
        if (mFile == null) {
            return false;
        }
        BufferedInputStream buffInput = null;
        BufferedOutputStream buffOutput = null;
        buffInput = new BufferedInputStream(input, 1024);
        try {
            FileOutputStream output = new FileOutputStream(mFile);
            buffOutput = new BufferedOutputStream(output, 1024);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = buffInput.read(b)) != -1) {
                buffOutput.write(b, 0, len);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (buffOutput != null)
                try {
                    buffOutput.close();// 关闭装饰流会自动关闭节点流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (buffInput != null)
                try {
                    buffInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return false;

    }

    /**
     * 读取指定目录中的文件
     *
     * @param path
     * @param name
     * @return 读取失败返回null
     */
    public static String readFile(String path, String name) {
        if (!check(path, name)) {
            return null;
        }
        if (checkFile(path, name) != FILE_EXIST_TRUE) {
            return null;
        }
        FileInputStream input = null;
        ByteArrayOutputStream out = null;
        File mFile = new File(path, name);
        if (!mFile.canRead()) {
            return null;
        }
        try {
            input = new FileInputStream(path + File.separator + name);
            out = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = input.read(b)) != -1) {
                out.write(b, 0, len);
            }
            return new String(out.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (input != null)
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    /**
     * 删除指定 路径的文件, name==null||"".equals(name)指定文件夹
     *
     * @param path
     * @param name
     * @return 删除成功true ,否则false
     */
    public static boolean delete(String path, String name) {
        if (!check(path)) {
            return false;
        }
        File mFile = null;
        if (name == null || "".equals(name)) {
            mFile = new File(path);
            if (!mFile.exists()) {
                return false;
            }
        } else {
            mFile = new File(path, name);
            if (!mFile.isFile()) {
                return false;
            } else {
                return mFile.delete();
            }
        }
        return deleteAll(mFile);
    }

    /**
     * 递归删除指定文件夹中所有的文件和目录和自己
     *
     * @param mFile
     * @return
     */
    private static boolean deleteAll(File mFile) {
        File[] files = mFile.listFiles();
        if (files != null && files.length != 0)
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete())
                        return false;
                } else {
                    deleteAll(file);
                }
            }
        if (!mFile.delete())
            return false;
        return true;
    }

    public static boolean writeFile(String path, String name, Bitmap bitmap) {
        if (!check(path, name, bitmap)) {
            return false;
        }
        int result = checkFile(path, name);
        File mFile = createFile(path, name, result);
        if (mFile != null) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(mFile);
                return bitmap.compress(Bitmap.CompressFormat.WEBP, 100, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    public static Bitmap readImage(String path, String name,
                                   BitmapFactory.Options opts) {
        if (!check(path, name)) {
            return null;
        }
        if (checkFile(path, name) != FILE_EXIST_TRUE) {
            return null;
        }
        return BitmapFactory.decodeFile(path + File.separator + name, opts);
    }

    /**
     * 一定指定路径的文件到 新的路径
     *
     * @param path
     * @param name
     * @param newPath
     * @return
     */
    public static boolean moveFile(String path, String name, String newPath) {
        if (!check(path, name, newPath)) {
            return false;
        }
        return moveFile(new File(path, name), newPath);
    }

    /**
     * 一定指定路径的文件到 新的路径
     *
     * @param newPath
     * @return
     */
    public static boolean moveFile(File oldFile, String newPath) {
        if (oldFile.exists() && oldFile.isFile()) {
            File mFile = new File(newPath);
            if (!mFile.exists()) {
                if (!mFile.mkdirs())
                    return false;
            }
            mFile = new File(mFile, oldFile.getName());
            oldFile.renameTo(mFile);

        }
        return false;
    }

    public static boolean renameFile(String path, String name, String newName) {
        if (!check(path, name, newName)) {
            return false;
        }
        return renameFile(new File(path, name), newName);
    }

    public static boolean renameFile(File oldFile, String newName) {
        if (oldFile.exists() && oldFile.isFile()) {
            File mFile = new File(oldFile.getParent(), newName);
            return oldFile.renameTo(mFile);
        }
        return false;

    }

    public static boolean renameToPath(String path, String name,
                                       String newPath, String newName) {
        if (!check(path, name, newPath, newName)) {
            return false;
        }
        return renameToPath(new File(path, name), newPath, newName);
    }

    public static boolean renameToPath(File oldFile, String newPath,
                                       String newName) {
        if (oldFile.exists() && oldFile.isFile()) {
            File mFile = new File(newPath);
            if (!mFile.exists()) {
                if (!mFile.mkdirs()) {
                    return false;
                }
            }
            File file_ = new File(mFile, newName);
            return oldFile.renameTo(file_);

        }
        return false;
    }

    public static File copyDirectory(String path, String newPath) {
        if (!check(path, newPath)) {
            return null;
        }
        return copyDirectory(new File(path), newPath);
    }

    public static File copyDirectory(File mFile, String newPath) {
        if (!mFile.isDirectory()) {
            return copyFile(mFile, newPath);
        }
        File file = new File(newPath, mFile.getName());
        int result = checkFile(file);
        file = createDirectory(file, result);
        if (file == null)
            return null;
        copyAllFiles(mFile, file.getAbsolutePath());
        return null;
    }

    private static boolean copyAllFiles(File mFile, String newPath) {
        File[] files = mFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                File file_ = new File(newPath, file.getName());
                if (!file_.exists())
                    if (!file_.mkdirs())
                        return false;
                copyAllFiles(file, file_.getAbsolutePath());
            } else {
                if (copyFile(file, newPath) == null)
                    return false;
            }
        }
        return true;
    }

    public static File copyAll(File mFile, String newPath) {
        return null;
    }

    public static File copyFile(File mFile, String newPath) {
        if (!mFile.isFile()) {
            return null;
        }
        if (!mFile.canRead()) {
            return null;
        }
        if (newPath == null || "".equals(newPath)) {
            return null;
        }
        File file = new File(newPath, mFile.getName());
        int result = checkFile(file);
        if (result == FILE_EXIST_TRUE)
            return file;
        file = createFile(file, result);
        if (file == null)
            return null;
        BufferedInputStream buffInput = null;
        BufferedOutputStream buffOutput = null;
        try {
            // 双缓冲
            FileInputStream input = new FileInputStream(mFile);
            FileOutputStream output = new FileOutputStream(file);
            buffInput = new BufferedInputStream(input, 1024);
            buffOutput = new BufferedOutputStream(output, 1024);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (buffInput != null && buffOutput != null) {
            byte[] b = new byte[1024];
            int len = 0;
            try {
                while ((len = buffInput.read(b)) != -1) {
                    buffOutput.write(b, 0, len);
                }
                return file;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (buffOutput != null) {
                    try {
                        buffOutput.close();// 关闭这个处理流会自动关闭节点流input
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (buffInput != null) {
                    try {
                        buffInput.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public static File copyFile(String path, String name, String newPath) {
        if (!check(path, name, newPath)) {
            return null;
        }
        return copyFile(new File(path, name), newPath);
    }

    /**
     * 获取最完善的SDCard路径
     * 1.如果有SDCard,并且给予访问权限
     *
     * @return
     */
    public static String getSDCardPath(Context mContext) {
        if (CheckUtil.isExistSDcard() && CheckUtil.checkSDCardPower(mContext)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else if (CheckUtil.isExistSDcard()) {
            return mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        }
        return null;
    }

    /**
     * 获取最完善的路径
     * 1.如果有SDCard,并且给予访问权限
     * 2.系统包名路径
     *
     * @return
     */
    public static String getPerfectionPath(Context mContext) {
        if (CheckUtil.isExistSDcard()) {
            return mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        }
        return mContext.getFilesDir().getAbsolutePath();

    }

    /**
     * 获取最完善的路径
     * 1.如果有SDCard,并且给予访问权限
     * 2.系统包名路径
     *
     * @return
     */
    public static String getPerfectionPath(Context mContext, String folderName) {
        String path = getPerfectionPath(mContext) + File.separator + folderName;
        createDirectory(path);
        return path;

    }

    public static boolean check(Object... params) {
        if (params == null || params.length == 0) {
            return false;
        }
        for (Object object : params) {
            if (object instanceof String) {
                String paramsStr = (String) object;
                if (paramsStr == null || "".equals(paramsStr)) {
                    return false;
                }
            } else if (object == null) {
                return false;
            }
        }
        return true;
    }

    private static int checkFile(Object... params) {
        if (params == null || params.length == 0) {
            return CHECK_FAIL;
        }
        Object obj = params[0];
        if (obj instanceof String && params.length > 1) {
            String path = (String) params[0];
            String name = (String) params[1];
            File mFile = new File(path);
            if (mFile.exists()) {
                if (mFile.isFile()) {
                    return FILE_FALSE_FLIEPATH_FAIL;
                }
                File file = new File(mFile, name);
                if (file.exists()) {
                    if (file.isFile()) {
                        return FILE_EXIST_TRUE;
                    } else if (file.isDirectory()) {
                        return FILE_FAIL_FILEpATH_TRUE;
                    } else {
                        return FILEPATH_EXIST_TRUE;
                    }
                } else {
                    return FILE_EXIST_FALSE;
                }
            } else {
                return FILEPATH_EXIST_FALSE;
            }

        } else if (obj instanceof File) {
            File mFile = (File) params[0];
            String path = mFile.getParent();
            String name = mFile.getName();
            if (path == null) {
                if (mFile.exists()) {
                    if (mFile.isFile()) {
                        return FILE_EXIST_TRUE;
                    } else if (mFile.isDirectory()) {
                        return FILEPATH_EXIST_TRUE;
                    } else {
                        return CHECK_FAIL;
                    }
                } else {
//                    return UNKNOWN;
                    return FILEPATH_EXIST_FALSE;
                }
            }
            return checkFile(path, name);

        }

        return CHECK_FAIL;
    }

    public static File download(String path, String newPath, String newName) {
        if (!check(path, newPath, newName)) {
            return null;
        }
        int result = checkFile(newPath, newName);
        if (result == FILE_EXIST_TRUE) {
            return new File(newPath, newName);
        }
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (connection != null) {
            try {
                writeFile(newPath,
                        (newName == null || "".equals(newName)) ? url.getPath()
                                : newName, connection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean fileIsExists(String filepath) {
        try {
            File f = new File(filepath);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }

    /**
     * Bitmap转化为byte数组
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        return out.toByteArray();
    }

    /**
     * byte数组转化为Bitmap
     *
     * @param bytes
     * @return
     */
    public static Bitmap bytes2Bitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}

