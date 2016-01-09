import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by breeze on 12/13/15.
 */
public class ReprotocForm {
    private JPanel panelRoot;
    private JTextField textFieldPath;
    private JButton buttonselect;
    private JLabel excuteScriptLabel;

    public ReprotocForm() {
        excuteScriptLabel.setLabelFor(textFieldPath);
        buttonselect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("请选择要执行的脚本文件");
                if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(null)) {
                    textFieldPath.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
    }


    public void setData(ReProtocComponent data) {
        textFieldPath.setText(data.getScriptpath());
    }

    public void getData(ReProtocComponent data) {
        data.setScriptpath(textFieldPath.getText());
    }

    public boolean isModified(ReProtocComponent data) {
        if (textFieldPath.getText() != null ? !textFieldPath.getText().equals(data.getScriptpath()) : data.getScriptpath() != null)
            return true;
        return false;
    }

    public JComponent getRootComponent() {
        return panelRoot;
    }
}
