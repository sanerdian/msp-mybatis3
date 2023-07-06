package com.jnetdata.msp.util;

import org.springframework.core.io.ClassPathResource;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FontUtil {
    public static Font getFont(){
        return getSelfDefinedFont("msyh.ttc");
    }

    public static Font getSelfDefinedFont(String fileName){
        Font font = null;
        try{
            ClassPathResource classPathResource = new ClassPathResource("fonts/"+fileName);
            InputStream inputStream =classPathResource.getInputStream();
            font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            font = font.deriveFont(Font.PLAIN, 20);
        }catch (FontFormatException e){
            return null;
        }catch (FileNotFoundException e){
            return null;
        }catch (IOException e){
            return null;
        }
        return font;
    }
}
