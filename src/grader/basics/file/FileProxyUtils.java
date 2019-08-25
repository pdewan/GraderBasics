package grader.basics.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import util.misc.Common;


public class FileProxyUtils {
    static public String toText(FileProxy f) {
        if (f == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        Scanner dataIn = new Scanner(f.getInputStream());
        boolean first = true;
        while (dataIn.hasNext()) {

            String nextLine = dataIn.nextLine();
            if (!first)
                sb.append("\n");
            Common.append(sb, nextLine);
            first = false;
        }
        return sb.toString();
    }

    static public List<String> toList(File f) {
        if (f == null) {
            return new ArrayList();
        }
        StringBuffer sb = new StringBuffer();
        try {
            return toList(new FileInputStream(f));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList();
    }

    static public List<String> toList(FileProxy f) {
        if (f == null) {
            return new ArrayList();
        }
        StringBuffer sb = new StringBuffer();
        return toList(f.getInputStream());
    }

    static public List<String> toList(InputStream input) {
        List<String> retVal = new ArrayList();

        Scanner dataIn = new Scanner(input);
        boolean first = true;
        while (dataIn.hasNext()) {

            String nextLine = dataIn.nextLine();
            retVal.add(nextLine);
        }
        return retVal;
    }
}
