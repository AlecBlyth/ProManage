package GUI_Classes;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class splashScreen extends JWindow {
    private int duration;
    public splashScreen(int d) {
        duration = d;
    }

    public void showIntro() throws IOException {

        String filePath = new File("src/Styling/images/logoIcon.png").getAbsolutePath(); //Logo Icon

        JPanel content = (JPanel)getContentPane(); //Background colour
        Color bg = new Color(38, 38, 38);
        Color sw = new Color(30,30,30);
        content.setBackground(bg);
        content.setBorder(BorderFactory.createLineBorder(sw,1));

        int width = 500; //Screen resolution adjustment
        int height = 250;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width-width)/2;
        int y = (screen.height-height)/2;
        setBounds(x,y, width, height);

        JLabel image = new JLabel(new ImageIcon(filePath));

        content.add(image, BorderLayout.CENTER);

        setVisible(true); //Display intro

        try {
            Thread.sleep(duration);
        } catch (Exception e){

        }
        setVisible(false); //Remove intro

    }

}
