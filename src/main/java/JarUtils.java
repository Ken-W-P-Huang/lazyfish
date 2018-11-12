import java.io.*;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 * Created by kenhuang on 2018/10/27.
 */
public class JarUtils {
    public static void packJar(String sourcePath, String jarPath) throws IOException {
        File source = new File(sourcePath);
        if (!source.exists()) {
            throw new IOException("Source doesn't exist!");
        } else if (!jarPath.endsWith(".jar")) {
            throw new IOException("JarPath must be path of jar file!");
        } else {
            java.util.jar.Manifest manifest = new java.util.jar.Manifest();
            manifest.getMainAttributes().put(java.util.jar.Attributes.Name.MANIFEST_VERSION, "1.0");
            BufferedInputStream bufferedInputStream = null;
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(jarPath), manifest);
            LinkedList<File> stack = new LinkedList<>();
            File[] files;
            String name;
            stack.push(source);
            try {
                while (stack.size() > 0) {
                    source = stack.pop();
                    name = source.getPath().replace("\\", "/");
                    if (source.isDirectory()) {
                        files = source.listFiles();
                        if (files != null) {
                            for (File nestedFile : files) {
                                stack.push(nestedFile);
                            }
                        }

                        if (!name.endsWith("/")) {
                            name += "/";
                        }
                    }
                    JarEntry entry = new JarEntry(name);
                    entry.setTime(source.lastModified());
                    jarOutputStream.putNextEntry(entry);
                    if (source.isFile()) {
                        bufferedInputStream = new BufferedInputStream(new FileInputStream(source));
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int count = bufferedInputStream.read(buffer);
                            if (count == -1) {
                                break;
                            }
                            jarOutputStream.write(buffer, 0, count);
                        }
                    }
                    jarOutputStream.closeEntry();
                }
            } finally {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                jarOutputStream.close();
            }
        }
    }

    public static void extractJar() {

    }
}
