import com.intellij.execution.TerminateRemoteProcessDialog;
import com.intellij.notification.EventLog;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.playback.commands.PrintCommand;
import com.intellij.openapi.util.*;
import com.intellij.remoteServer.agent.util.log.TerminalPipe;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.*;

/**
 * Created by breeze on 12/13/15.
 */
public class ReProtocComponent implements ApplicationComponent, Configurable, JDOMExternalizable {
    public String scriptpath;
    private ReprotocForm form;

    public ReProtocComponent() {
        scriptpath = "please select your config path";
    }

    @Override
    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    @Override
    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "ReProtocComponent";
    }
    //保存字符串到文件中
    private void saveAsFileWriter(String content) {

        File file = new File(content);
        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(file);
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void doReProtoc() {
//        Messages.showMessageDialog(scriptpath == null ? "no config" : scriptpath,
//                scriptpath == null ? "no config" : scriptpath, Messages.getInformationIcon());
//        String msg = runCommand("sh /Users/breeze/workspace/outsource_projects/BuyCars/protocol/android_make_bc.sh", 2);
//        String msg = runCommand("sh " + scriptpath, 2);
//        String msg = runCommand("sh " + scriptpath, 2);
        String msg = process(scriptpath, "");
//        Messages.showMessageDialog(msg, scriptpath, Messages.getInformationIcon());
        Messages.showInfoMessage(msg, scriptpath);
        System.out.println(msg);
//        saveAsFileWriter(msg);

//        try {
////            Runtime.getRuntime().exec(scriptpath).waitFor();
//            Runtime runtime = Runtime.getRuntime();
//            runtime.exec("sh /Users/breeze/workspace/outsource_projects/BuyCars/protocol/android_make_bc.sh");
//            Messages.showMessageDialog("Success", "you are very good", Messages.getInformationIcon());
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////            Messages.showMessageDialog("Error", e.getMessage(), Messages.getInformationIcon());
//        } catch (IOException e) {
//            e.printStackTrace();
//            Messages.showMessageDialog("Error", e.getMessage(), Messages.getInformationIcon());
//        }
    }
    private String process(String command, String filterContent) {
        try {
            String[] cmd = {"/bin/sh", "-c", command};
            Process p = Runtime.getRuntime().exec(cmd);
            DataInputStream pStdout = new DataInputStream(p.getInputStream());
            DataOutputStream pStdin = new DataOutputStream(p.getOutputStream());

            pStdin.write(filterContent.getBytes());
            pStdin.close();
            return cat(pStdout);
        } catch (Exception e) {
            System.err.println("Got exception: " + e);
        }
        return null;
    }
    public static String cat(InputStream is) throws IOException {
        byte[] buf = new byte[65536];
        int offset = 0;
        int bytesRead;
        while ((bytesRead = is.read(buf, offset, 8192)) != -1) {
            offset += bytesRead;
            if (offset + 8192 >= buf.length) {
                byte[] newBuf = new byte[buf.length * 3];
                System.arraycopy(buf, 0, newBuf, 0, offset);
                buf = newBuf;
            }
        }
        return new String(buf, 0, offset);
    }

    private String runCommand(String cmd, int tp) {
        StringBuffer buf = new StringBuffer(1000);
        String rt = "-1";
        try {
            Process pos = Runtime.getRuntime().exec(cmd);
            pos.waitFor();
            if (tp == 1) {
                if (pos.exitValue() == 0) {
                    rt = "1";
                }
            } else {
                InputStreamReader ir = new InputStreamReader(pos.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);
                String ln = "";
                while ((ln = input.readLine()) != null) {
                    buf.append(ln + "<br>\n");
                }
                rt = buf.toString();
                input.close();
                ir.close();
            }
        } catch (java.io.IOException e) {
            rt = e.toString();
        } catch (Exception e) {
            rt = e.toString();
        }
        return rt;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "replace protocbuf config";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (form == null) {
            form = new ReprotocForm();
        }
        return form.getRootComponent();
    }

    @Override
    public boolean isModified() {
        return form != null && form.isModified(this);
    }

    @Override
    public void apply() throws ConfigurationException {
        if (form != null) {
            form.getData(this);
        }
    }

    @Override
    public void reset() {
        if (form != null) {
            form.setData(this);
        }
    }

    @Override
    public void disposeUIResources() {
        form = null;
    }

    public String getScriptpath() {
        return scriptpath;
    }

    public void setScriptpath(final String scriptpath) {
        this.scriptpath = scriptpath;
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        DefaultJDOMExternalizer.readExternal(this, element);
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        DefaultJDOMExternalizer.writeExternal(this, element);
    }
}
