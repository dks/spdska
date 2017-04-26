import com.jdotsoft.jarloader.JarClassLoader;

public class AppLauncher {
    public static void main(String[] args) {
        JarClassLoader jcl = new JarClassLoader();
        try {
            jcl.invokeMain("Main", args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    } // main()
} // class MyAppLauncher
