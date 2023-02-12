package com.company;
import java.awt.event.*;
import java.math.BigInteger;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class DecTo64 implements ActionListener {
    JFrame frame;
    JPanel panel, round;
    JTextField textField;
    JLabel out, bin, binOut, hex, hexOut, signLbl, combiLbl, expCLbl, coCLbl, signOut, combiOut, expCOut, coCOut, fileLoc, group, mem1, mem2, mem3, mem4;

    JButton[] funButtons = new JButton[6];
    JButton[] numButtons = new JButton[10];
    JButton[] roundButtons = new JButton[4];
    JButton decButton, expButton, delButton, clrButton, negButton, conButton, negExpButton;
    JButton rUpButton, rDownButton, truncButton, rToNButton;
    JButton outButton;

    Font font = new Font("Arial", Font.BOLD, 30);
    Font small = new Font("Arial", Font.BOLD, 20);
    Font smallest = new Font("Arial", Font.BOLD, 17);

    boolean special = false;

    public class Globals{
        public static int exponent;
        public static boolean decimalpoint;
        public static boolean padded = false;
    }

    public DecTo64() {
        this.frame = new JFrame("IEEE-754 Decimal 64 Floating-Point Converter");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(1280, 720);
        this.frame.setResizable(false);
        this.frame.setLayout(null);
        this.frame.setLocationRelativeTo(null);

        this.textField = new JTextField();
        this.textField.setBounds(50, 25, 300, 50);
        this.textField.setFont(this.small);
        this.textField.setEditable(false); //edit later if needed
        this.frame.add(this.textField);

        this.out = new JLabel("OUTPUT");
        this.out.setBounds(800, 25, 300, 50);
        this.out.setFont(this.font);
        this.frame.add(this.out);

        this.panel = new JPanel();
        this.panel.setBounds(50, 100, 300, 300);
        this.panel.setLayout(new GridLayout(4, 4, 1, 1));
        this.panel.setBackground(this.frame.getBackground());

        this.round = new JPanel();
        this.round.setBounds(50, 510, 300, 120);
        this.round.setLayout(new GridLayout(4, 1, 1, 1));
        this.round.setBackground(this.frame.getBackground());

        this.delButton = new JButton("DEL");
        this.clrButton = new JButton("CLR");
        this.negButton = new JButton("+/-");
        this.decButton = new JButton(".");
        this.expButton = new JButton("x10");
        this.negExpButton = new JButton("-exp");

        this.funButtons[0] = this.delButton;
        this.funButtons[1] = this.clrButton;
        this.funButtons[2] = this.negButton;
        this.funButtons[3] = this.decButton;
        this.funButtons[4] = this.expButton;
        this.funButtons[5] = this.negExpButton;

        this.rUpButton = new JButton("ROUND UP");
        this.rDownButton = new JButton("ROUND DOWN");
        this.truncButton = new JButton("TRUNCATE");
        this.rToNButton = new JButton("ROUND TO NEAREST");

        this.roundButtons[0] = this.rUpButton;
        this.roundButtons[1] = this.rDownButton;
        this.roundButtons[2] = this.truncButton;
        this.roundButtons[3] = this.rToNButton;

        for(int i = 0; i < 6; i++) {
            this.funButtons[i].addActionListener(this);
            this.funButtons[i].setFont(this.smallest);
            this.funButtons[i].setFocusable(false);
            if(i == 0 || i == 1) {
                this.funButtons[i].setBackground(Color.red);
                this.funButtons[i].setForeground(Color.white);
            }
        }

        for(int i = 0; i < 4; i++) {
            this.roundButtons[i].addActionListener(this);
            this.roundButtons[i].setFont(this.small);
            this.roundButtons[i].setFocusable(false);
            this.round.add(this.roundButtons[i]);
        }

        for(int i = 0; i < 10; i++) {
            this.numButtons[i] = new JButton(String.valueOf(i));
            this.numButtons[i].addActionListener(this);
            this.numButtons[i].setFont(this.font);
            this.numButtons[i].setFocusable(false);
        }

        this.conButton = new JButton("CONVERT");
        this.conButton.addActionListener(this);
        this.conButton.setFont(this.small);
        this.conButton.setFocusable(false);
        this.conButton.setBounds(125, 430, 145, 50);
        this.frame.add(this.conButton);

        this.panel.add(this.numButtons[1]);
        this.panel.add(this.numButtons[2]);
        this.panel.add(this.numButtons[3]);
        this.panel.add(this.funButtons[0]);

        this.panel.add(this.numButtons[4]);
        this.panel.add(this.numButtons[5]);
        this.panel.add(this.numButtons[6]);
        this.panel.add(this.funButtons[1]);

        this.panel.add(this.numButtons[7]);
        this.panel.add(this.numButtons[8]);
        this.panel.add(this.numButtons[9]);
        this.panel.add(this.funButtons[2]);

        this.panel.add(this.funButtons[3]);
        this.panel.add(this.numButtons[0]);
        this.panel.add(this.funButtons[4]);
        this.panel.add(this.funButtons[5]);

        this.bin = new JLabel("BINARY:");
        this.bin.setBounds(400, 100, 150, 120);
        this.bin.setFont(this.font);
        this.frame.add(this.bin);

        this.signLbl = new JLabel("sign bit -");
        this.signLbl.setBounds(535, 115, 195, 100);
        this.signLbl.setFont(this.smallest);
        this.signLbl.setVisible(false);
        this.frame.add(this.signLbl);

        this.signOut = new JLabel();
        this.signOut.setBounds(735, 115, 490, 100);
        this.signOut.setFont(this.smallest);
        this.signOut.setVisible(false);
        this.frame.add(this.signOut);

        this.combiLbl = new JLabel("combination field -");
        this.combiLbl.setBounds(535, 150, 195, 100);
        this.combiLbl.setFont(this.smallest);
        this.combiLbl.setVisible(false);
        this.frame.add(this.combiLbl);

        this.combiOut = new JLabel();
        this.combiOut.setBounds(735, 150, 490, 100);
        this.combiOut.setFont(this.smallest);
        this.combiOut.setVisible(false);
        this.frame.add(this.combiOut);

        this.expCLbl = new JLabel("exponent continuation -");
        this.expCLbl.setBounds(535, 185, 195, 100);
        this.expCLbl.setFont(this.smallest);
        this.expCLbl.setVisible(false);
        this.frame.add(this.expCLbl);

        this.expCOut = new JLabel();
        this.expCOut.setBounds(735, 185, 490, 100);
        this.expCOut.setFont(this.smallest);
        this.expCOut.setVisible(false);
        this.frame.add(this.expCOut);

        this.coCLbl = new JLabel("coefficient continuation -");
        this.coCLbl.setBounds(535, 220, 195, 100);
        this.coCLbl .setFont(this.smallest);
        this.coCLbl.setVisible(false);
        this.frame.add(this.coCLbl);

        this.coCOut = new JLabel();
        this.coCOut.setBounds(735, 220, 490, 100);
        this.coCOut.setFont(this.smallest);
        this.coCOut.setVisible(false);
        this.frame.add(this.coCOut);

        this.hex = new JLabel("HEX:");
        this.hex.setBounds(450, 300, 150, 120);
        this.hex.setFont(this.font);
        this.frame.add(this.hex);

        this.binOut = new JLabel();
        this.binOut.setBounds(535, 100, 700, 120);
        this.binOut.setFont(this.small);
        this.binOut.setVisible(false);
        this.frame.add(this.binOut);

        this.hexOut = new JLabel();
        this.hexOut.setBounds(535, 300, 700, 120);
        this.hexOut.setFont(this.small);
        this.frame.add(this.hexOut);

        this.fileLoc = new JLabel();
        this.fileLoc.setBounds(535, 400, 680, 120);
        this.fileLoc.setFont(this.smallest);
        this.fileLoc.setVisible(false);
        this.frame.add(this.fileLoc);

        this.outButton = new JButton("OUTPUT TO TEXT FILE");
        this.outButton.addActionListener(this);
        this.outButton.setBounds(700, 500, 300, 100);
        this.outButton.setFont(this.small);
        this.outButton.setFocusable(false);
        this.frame.add(this.outButton);

        this.group = new JLabel("S11 Group 8");
        this.group.setBounds(1050, 450, 150, 120);
        this.group.setFont(this.small);
        this.frame.add(this.group);

        this.mem1 = new JLabel("Herrera, Arquiel");
        this.mem1.setBounds(1050, 475, 150, 120);
        this.mem1.setFont(this.small);
        this.frame.add(this.mem1);

        this.mem2 = new JLabel("Muros, Don");
        this.mem2.setBounds(1050, 500, 150, 120);
        this.mem2.setFont(this.small);
        this.frame.add(this.mem2);

        this.mem3 = new JLabel("Sibal, Marc");
        this.mem3.setBounds(1050, 525, 150, 120);
        this.mem3.setFont(this.small);
        this.frame.add(this.mem3);

        this.mem4 = new JLabel("Tan, Patrick");
        this.mem4.setBounds(1050, 550, 150, 120);
        this.mem4.setFont(this.small);
        this.frame.add(this.mem4);

        this.frame.add(this.panel);
        this.frame.add(this.round);
        this.frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //number buttons
        for(int i = 0; i < 10; i++) {
            if(e.getSource() == this.numButtons[i]) {
                if(!this.textField.getText().isEmpty() && !this.textField.getText().contains("x10^")) {
                    String str = this.textField.getText();
                    int length = str.length();
                    char first = str.charAt(0);

                    if(length == 1) {
                        if(Character.compare(first, '0') != 0)
                            this.textField.setText(str.concat(String.valueOf(i)));
                        else if(i > 0 && Character.compare(first, '0') == 0)
                            this.textField.setText(String.valueOf(i));
                    }
                    else {
                        char second = str.charAt(1);

                        if(Character.compare(first, '-') == 0 && Character.compare(second, '0') != 0)
                            this.textField.setText(str.concat(String.valueOf(i)));
                        else if(Character.compare(first, '-') == 0 && Character.compare(second, '0') == 0 && i == 0)
                            break;
                        else if(Character.compare(first, '-') == 0 && Character.compare(second, '0') == 0 && i > 0)
                            this.textField.setText(str.charAt(0) + String.valueOf(i));
                        else
                            this.textField.setText(str.concat(String.valueOf(i)));
                    }
                }
                else if(!this.textField.getText().isEmpty() && this.textField.getText().contains("x10^")){
                    String str = this.textField.getText();
                    char secondToLast = str.charAt(str.length()-2);
                    char last = str.charAt(str.length()-1);

                    if(i == 0 && str.contains("x10^") && ((Character.compare(last, '0') == 0 && Character.compare(secondToLast, '^') == 0) ||
                        (Character.compare(last, '0') == 0 && Character.compare(secondToLast, '-') == 0 && Character.compare(str.charAt(str.length()-3), '^') == 0)))
                        break;
                    else if(i > 0 && Character.compare(secondToLast, '^') == 0 && Character.compare(last, '0') == 0)
                        this.textField.setText(this.textField.getText().substring(0, this.textField.getText().length()-1) + String.valueOf(i));
                    else
                        this.textField.setText(this.textField.getText().concat(String.valueOf(i)));
                }
                else
                    this.textField.setText(this.textField.getText().concat(String.valueOf(i)));

            }
        }
        //delete button
        if(e.getSource() == this.delButton && !this.textField.getText().isEmpty()) {
            String str = this.textField.getText();
            char last = str.charAt(str.length()-1);

            if(this.textField.getText().contains("x10^") && Character.compare(last, '^') == 0)
                this.textField.setText(str.replace("x10^", ""));
            else
                this.textField.setText(str.substring(0, str.length()-1));
        }
        //clear button
        if(e.getSource() == this.clrButton && !this.textField.getText().isEmpty()) {
            this.textField.setText("");
        }
        //negative button
        if(e.getSource() == this.negButton && !this.textField.getText().isEmpty()) {
            String str = this.textField.getText();
            char first = str.charAt(0);

            if(Character.compare(first, '-') == 0)
                this.textField.setText(str.substring(1));
            else
                this.textField.setText("-".concat(str));
        }
        //decimal point button
        if(e.getSource() == this.decButton && this.textField.getText().indexOf('.') == -1 && !this.textField.getText().contains("x10^")) {
            if(this.textField.getText().equals(""))
                this.textField.setText("0.");
            else
                this.textField.setText(this.textField.getText().concat("."));
        }
        //base-10 button
        if(e.getSource() == this.expButton && !this.textField.getText().isEmpty() && !this.textField.getText().contains("x10^")) {
            String str = this.textField.getText();
            char last = str.charAt(str.length()-1);

            if(str.contains(".")) {
                int index = str.indexOf(".");

                if(Character.compare(last, '.') == 0)
                    this.textField.setText(this.textField.getText().concat("0x10^"));
                else if(Character.compare(last, '.') != 0 && Character.compare(str.charAt(index+1), '0') == 0) {
                    String subString = str.substring(index+1);
                    int length = subString.length();
                    boolean isZero = true;

                    for(int i = 0; i < length; i++) {
                        if(Character.compare(subString.charAt(i), '0') != 0) {
                            isZero = false;
                            break;
                        }
                    }

                    if(isZero)
                        this.textField.setText(str.substring(0, index+1) + "0x10^");
                    else
                        this.textField.setText(this.textField.getText().concat("x10^"));
                }
                else
                    this.textField.setText(this.textField.getText().concat("x10^"));
            }
            else
                this.textField.setText(this.textField.getText().concat("x10^"));
        }
        //negative exponent button
        if(e.getSource() == this.negExpButton && !this.textField.getText().isEmpty() && this.textField.getText().contains("x10^")) {
            String str = this.textField.getText();
            String subString1, subString2;
            char last = str.charAt(str.length()-1);
            char first;
            int exp = 0;

            if(Character.compare(last, '^') != 0) {
                exp = str.indexOf('^') + 1;
                subString1 = str.substring(0, exp);
                subString2 = str.substring(exp);

                first = subString2.charAt(0);

                str = subString1;
                if(Character.compare(first, '-') == 0)
                    str = str + subString2.substring(1);
                else
                    str = str + "-" + subString2;

                this.textField.setText(str);
            }
        }
        //output to text file
        if(e.getSource() == this.outButton && !this.textField.getText().isEmpty() && !this.hexOut.getText().isEmpty()) {

            try {
                File file = new File("output.txt");
                if(file.createNewFile()) {
                    System.out.println("Created file at: " + file.getCanonicalPath());
                    this.fileLoc.setText("Created file at: " + file.getCanonicalPath());
                }
                else {
                    System.out.println("Updated file at: " + file.getCanonicalPath());
                    this.fileLoc.setText("Updated file at: " + file.getCanonicalPath());
                }

                this.fileLoc.setVisible(true);

                FileWriter fw = new FileWriter("output.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw);

                out.println("Input: " + this.textField.getText() + "\n");

                if(this.special)
                    out.println("Binary Output: " + this.binOut.getText());
                else {
                    out.println("Binary Output:" + this.binOut.getText());
                    out.println("\t\tSign Bit:\t\t  " + this.signOut.getText());
                    out.println("\t\tCombinational Field:\t  " + this.combiOut.getText());
                    out.println("\t\tExponent Continuation:\t  " + this.expCOut.getText());
                    out.println("\t\tCoefficient Continuation: " + this.coCOut.getText());
                }
                out.println("\nHexadecimal Equivalent: " + this.hexOut.getText() + "\n\n");

                out.close();
                bw.close();
                fw.close();
            } catch(IOException error) {
                error.getStackTrace();
            }
        }
        //round up button
        if(e.getSource() == this.rUpButton && !this.textField.getText().isEmpty() && this.textField.getText().contains("x10^")) {
            String str = this.textField.getText();
            int x = str.indexOf("x");
            int caret = str.indexOf("^");
            String subString1, subString2, subString3;
            int exp = 0;

            subString1 = str.substring(0, x);
            subString2 = str.substring(x, caret+1);
            subString3 = str.substring(caret+1);

            exp = Integer.parseInt(subString3);
            Globals.exponent = exp;

            subString1 = RemoveDecPoint(subString1, exp);
            exp = Globals.exponent;
            subString1 = NormalizeDec(subString1);
            exp = NormalizeExp(subString1, exp);
            subString3 = String.valueOf(exp);

            subString1 = RoundUp(subString1);

            Globals.padded = false;

            this.textField.setText(subString1 + subString2 + subString3);
        }
        //round down
        if(e.getSource() == this.rDownButton && !this.textField.getText().isEmpty() && this.textField.getText().contains("x10^")) {
            String str = this.textField.getText();
            int x = str.indexOf("x");
            int caret = str.indexOf("^");
            String subString1, subString2, subString3;
            int exp = 0;

            subString1 = str.substring(0, x);
            subString2 = str.substring(x, caret+1);
            subString3 = str.substring(caret+1);

            exp = Integer.parseInt(subString3);
            Globals.exponent = exp;

            subString1 = RemoveDecPoint(subString1, exp);
            exp = Globals.exponent;
            subString1 = NormalizeDec(subString1);
            exp = NormalizeExp(subString1, exp);
            subString3 = String.valueOf(exp);

            subString1 = RoundDown(subString1);

            Globals.padded = false;

            this.textField.setText(subString1 + subString2 + subString3);
        }
        //truncate button
        if(e.getSource() == this.truncButton && !this.textField.getText().isEmpty() && this.textField.getText().contains("x10^")) {
            String str = this.textField.getText();
            int x = str.indexOf("x");
            int caret = str.indexOf("^");
            String subString1, subString2, subString3;
            int exp = 0;

            subString1 = str.substring(0, x);
            subString2 = str.substring(x, caret+1);
            subString3 = str.substring(caret+1);

            exp = Integer.parseInt(subString3);
            Globals.exponent = exp;

            subString1 = RemoveDecPoint(subString1, exp);
            exp = Globals.exponent;
            subString1 = NormalizeDec(subString1);
            exp = NormalizeExp(subString1, exp);
            subString3 = String.valueOf(exp);

            subString1 = Truncate(subString1);

            Globals.padded = false;

            this.textField.setText(subString1 + subString2 + subString3);
        }
        //round to nearest
        if(e.getSource() == this.rToNButton && !this.textField.getText().isEmpty() && this.textField.getText().contains("x10^")) {
            String str = this.textField.getText();
            int x = str.indexOf("x");
            int caret = str.indexOf("^");
            String subString1, subString2, subString3;
            int exp = 0;

            subString1 = str.substring(0, x);
            subString2 = str.substring(x, caret+1);
            subString3 = str.substring(caret+1);

            exp = Integer.parseInt(subString3);
            Globals.exponent = exp;

            subString1 = RemoveDecPoint(subString1, exp);
            exp = Globals.exponent;
            subString1 = NormalizeDec(subString1);
            exp = NormalizeExp(subString1, exp);
            subString3 = String.valueOf(exp);

            subString1 = RoundNearest(subString1);

            Globals.padded = false;

            this.textField.setText(subString1 + subString2 + subString3);
        }
        //convert button
        if(e.getSource() == this.conButton && !this.textField.getText().isEmpty() && this.textField.getText().contains("x10^")) {
            String finalAns = new String();
            String toBCDString;
            String[] BCDList;
            String dec_Inp;
            int exp;

            String str = this.textField.getText();
            char last = str.charAt(str.length()-1);
            int x = str.indexOf('x');
            System.out.println(x);
            int caret = str.indexOf('^');
            System.out.println(caret);

            dec_Inp = str.substring(0, x);
            System.out.println(dec_Inp);

            this.signLbl.setVisible(false);
            this.signOut.setVisible(false);
            this.combiLbl.setVisible(false);
            this.combiOut.setVisible(false);
            this.expCLbl.setVisible(false);
            this.expCOut.setVisible(false);
            this.coCLbl.setVisible(false);
            this.coCOut.setVisible(false);
            this.binOut.setVisible(false);
            this.fileLoc.setVisible(false);

            if(Character.compare(last, '^') == 0) {
                exp = 0;
                this.textField.setText(this.textField.getText().concat("0"));
            }
            else
                exp = Integer.parseInt(str.substring(caret+1));
            Globals.exponent = exp;
            System.out.println(exp);

            dec_Inp = RemoveDecPoint(dec_Inp, exp);
            exp = Globals.exponent;
            dec_Inp = NormalizeDec(dec_Inp);
            exp = NormalizeExp(dec_Inp, exp);
            System.out.println(dec_Inp);

            if (dec_Inp.charAt(0) == '-'){
                toBCDString = dec_Inp.substring(2);
            }
            else{
                toBCDString = dec_Inp.substring(1);
            }

            System.out.println("test3");
            String sign = getSignBit(dec_Inp);
            String combi = getCombiField(dec_Inp, exp);
            System.out.println("test11");
            String expCont = getExpCont(exp);
            System.out.println("test4");

            if (combi.equals("INFINITY")){
                if (sign.equals("0"))
                    finalAns = "+INFINITY";
                else
                    finalAns = "-INFINITY";

                    System.out.println("test12");
                this.binOut.setText(finalAns);
                this.binOut.setVisible(true);
                this.hexOut.setText(finalAns);
                this.special = true;
            }
            else if(combi.equals("NaN")){
                finalAns = "NaN";
                this.binOut.setText(finalAns);
                this.binOut.setVisible(true);
                this.hexOut.setText(finalAns);
                this.special = true;
            }
            else{
                System.out.println("test5");
                finalAns = finalAns + sign;
                finalAns = finalAns + combi;
                finalAns = finalAns + expCont;

                this.signLbl.setVisible(true);
                this.signOut.setVisible(true);
                this.combiLbl.setVisible(true);
                this.combiOut.setVisible(true);
                this.expCLbl.setVisible(true);
                this.expCOut.setVisible(true);
                this.coCLbl.setVisible(true);
                this.coCOut.setVisible(true);


                System.out.println("tobcdstring = " + toBCDString);
                BCDList = toBCDString.split("(?<=\\G.{" + 3 + "})");
                for (int i = 0; i < 5; i++) {
                    String bcd = DecToBCD(BCDList[i]);
                    System.out.println(BCDList[i]);
                    String dpbcd = BCDToDPBCD(bcd);

                    finalAns = finalAns + dpbcd;
                }

                String hexString = new BigInteger(finalAns, 2).toString(16);

                if (hexString.length() != 16){
                    while (hexString.length() != 16){
                        hexString = "0" + hexString;
                    }
                }

                hexString = hexString.toUpperCase();
                hexString = hexString.substring(0, 4) + " " + hexString.substring(4, 8) + " " +
                            hexString.substring(8, 12) + " " + hexString.substring(12);

                combi = combi.substring(0, 2) + " " + combi.substring(2);
                expCont = expCont.substring(0, 4) + " " + expCont.substring(4);
                String coefCont = finalAns.substring(14, 24) + " " + finalAns.substring(24, 34) + " " +
                                finalAns.substring(34, 44) + " " + finalAns.substring(44, 54) + " " +
                                finalAns.substring(54);

                System.out.println(finalAns);
                System.out.println(hexString.toUpperCase());
                this.signOut.setText(sign);
                this.combiOut.setText(combi);
                this.expCOut.setText(expCont);
                this.coCOut.setText(coefCont);
                this.hexOut.setText(hexString);
                this.special = false;
            }
        }
    }

    static String BCDToDPBCD (String BCDVals){
        char[] output = new char[10];
        char a = BCDVals.charAt(0);
        char b = BCDVals.charAt(1);
        char c = BCDVals.charAt(2);
        char d = BCDVals.charAt(3);
        char e = BCDVals.charAt(4);
        char f = BCDVals.charAt(5);
        char g = BCDVals.charAt(6);
        char h = BCDVals.charAt(7);
        char i = BCDVals.charAt(8);
        char j = BCDVals.charAt(9);
        char k = BCDVals.charAt(10);
        char m = BCDVals.charAt(11);

        if(a == '0' && e == '0' && i == '0'){
            output[0] = b; output[1] = c; output[2] = d; output[3] = f; output[4] = g;
            output[5] = h; output[6] = '0'; output[7] = j; output[8] = k; output[9] = m;
        }
        else if(a == '0' && e == '0' && i == '1'){
            output[0] = b; output[1] = c; output[2] = d; output[3] = f; output[4] = g;
            output[5] = h; output[6] = '1'; output[7] = '0'; output[8] = '0'; output[9] = m;
        }
        else if(a == '0' && e == '1' && i == '0'){
            output[0] = b; output[1] = c; output[2] = d; output[3] = j; output[4] = k;
            output[5] = h; output[6] = '1'; output[7] = '0'; output[8] = '1'; output[9] = m;
        }
        else if(a == '0' && e == '1' && i == '1'){
            output[0] = b; output[1] = c; output[2] = d; output[3] = '1'; output[4] = '0';
            output[5] = h; output[6] = '1'; output[7] = '1'; output[8] = '1'; output[9] = m;
        }
        else if(a == '1' && e == '0' && i == '0'){
            output[0] = j; output[1] = k; output[2] = d; output[3] = f; output[4] = g;
            output[5] = h; output[6] = '1'; output[7] = '1'; output[8] = '0'; output[9] = m;
        }
        else if(a == '1' && e == '0' && i == '1'){
            output[0] = f; output[1] = g; output[2] = d; output[3] = '0'; output[4] = '1';
            output[5] = h; output[6] = '1'; output[7] = '1'; output[8] = '1'; output[9] = m;
        }
        else if(a == '1' && e == '1' && i == '0'){
            output[0] = j; output[1] = k; output[2] = d; output[3] = '0'; output[4] = '0';
            output[5] = h; output[6] = '1'; output[7] = '1'; output[8] = '1'; output[9] = m;
        }
        else if(a == '1' && e == '1' && i == '1'){
            output[0] = '0'; output[1] = '0'; output[2] = d; output[3] = '1'; output[4] = '1';
            output[5] = h; output[6] = '1'; output[7] = '1'; output[8] = '1'; output[9] = m;
        }

        String DPBCD = String.copyValueOf(output);
        return DPBCD;
    }

    static String DecToBCD (String DecVals){
        char[] output = new char[12];

        int j = 0;
        for (int i=0; i<3; i++){

            if(DecVals.charAt(i) == '0'){
                output[j] = '0'; output[j+1] = '0'; output[j+2] = '0'; output[j+3] = '0';
                j+=4;
            }
            if(DecVals.charAt(i) == '1'){
                output[j] = '0'; output[j+1] = '0'; output[j+2] = '0'; output[j+3] = '1';
                j+=4;
            }
            if(DecVals.charAt(i) == '2'){
                output[j] = '0'; output[j+1] = '0'; output[j+2] = '1'; output[j+3] = '0';
                j+=4;
            }
            if(DecVals.charAt(i) == '3'){
                output[j] = '0'; output[j+1] = '0'; output[j+2] = '1'; output[j+3] = '1';
                j+=4;
            }
            if(DecVals.charAt(i) == '4'){
                output[j] = '0'; output[j+1] = '1'; output[j+2] = '0'; output[j+3] = '0';
                j+=4;
            }
            if(DecVals.charAt(i) == '5'){
                output[j] = '0'; output[j+1] = '1'; output[j+2] = '0'; output[j+3] = '1';
                j+=4;
            }
            if(DecVals.charAt(i) == '6'){
                output[j] = '0'; output[j+1] = '1'; output[j+2] = '1'; output[j+3] = '0';
                j+=4;
            }
            if(DecVals.charAt(i) == '7'){
                output[j] = '0'; output[j+1] = '1'; output[j+2] = '1'; output[j+3] = '1';
                j+=4;
            }
            if(DecVals.charAt(i) == '8'){
                output[j] = '1'; output[j+1] = '0'; output[j+2] = '0'; output[j+3] = '0';
                j+=4;
            }
            if(DecVals.charAt(i) == '9'){
                output[j] = '1'; output[j+1] = '0'; output[j+2] = '0'; output[j+3] = '1';
                j+=4;
            }
        }
        String BCD = String.copyValueOf(output);
        return BCD;
    }

    static String getSignBit (String DecInput){
        String signBit;
        if (DecInput.charAt(0) == '-'){
            signBit = "1";
        }
        else{
            signBit = "0";
        }

        System.out.println("test6");
        return signBit;
    }

    static String getCombiField (String DecInput, int exp){
        char msd;
        System.out.println(exp);
        exp += 398;
        String exp_bin = Integer.toBinaryString(exp);
        char[] combiField = new char[5];

        System.out.println("exp_bin = " + exp_bin);
        System.out.println("exp = " + exp);

        if (exp_bin.length() > 10){
            exp_bin = "1111111111";
        }

        System.out.println("test9");
        if (exp_bin.length() != 10){
            while(exp_bin.length() != 10){
                exp_bin = '0' + exp_bin;
            }
        }
        System.out.println("mod = " + exp_bin);
        System.out.println("test10");
        if (DecInput.charAt(0) == '-'){
            msd = DecInput.charAt(1);
        }
        else{
            msd = DecInput.charAt(0);
        }

        String msd_bin = Integer.toBinaryString(Character.getNumericValue(msd));
        
        if (msd_bin.length() < 4){
            while(msd_bin.length() < 4){
                msd_bin = '0' + msd_bin;
            }
        }
        System.out.println("test8");
        if (Character.getNumericValue(msd) < 8){
            combiField[0] = exp_bin.charAt(0);
            combiField[1] = exp_bin.charAt(1);
            combiField[2] = msd_bin.charAt(1);
            combiField[3] = msd_bin.charAt(2);
            combiField[4] = msd_bin.charAt(3);
        }
        else if (Character.getNumericValue(msd) > 7){
            combiField[0] = '1';
            combiField[1] = '1';
            combiField[2] = exp_bin.charAt(0);
            combiField[3] = exp_bin.charAt(1);
            combiField[4] = msd_bin.charAt(3);
        }

        String result = String.copyValueOf(combiField);
        System.out.println("test1");
        if (exp_bin.length() > 10 || exp < 0 || exp > 767){
            result = "11110";
        }
        System.out.println("test2");
        if (result.equals("11110")){
            result = "INFINITY";
        }
        else if (result.equals("11111")){
            result = "NaN";
        }

        System.out.println(result);
        return result;
    }

    static String getExpCont (int exp){
        exp += 398;
        String exp_bin = Integer.toBinaryString(exp);
        String result;

        if (exp_bin.length() > 10){
            exp_bin = "1111111111";
        }

        if (exp_bin.length() != 10){
            while(exp_bin.length() != 10){
                exp_bin = '0' + exp_bin;
            }
        }

        System.out.println("test7");
        result = exp_bin.substring(2);
        return result;
    }

    static String RemoveDecPoint (String DecInput, int exp){
        for(int i=0;i<DecInput.length();i++){
            if(DecInput.charAt(i) == '.'){
                Globals.decimalpoint = true ;
                int index = i;
                StringBuilder sb = new StringBuilder(DecInput);
                sb.deleteCharAt(i);
                DecInput = sb.toString();

                if(i > 17 && DecInput.charAt(0) == '-'){
                    break;
                }
                else if(i > 16 && DecInput.charAt(0) != '-'){
                    break;
                }
                else{
                    Globals.exponent = exp - (DecInput.length() - index);
                }
            }
        }
        return DecInput;
    }
    // Not a number input
    // special cases

    static String NormalizeDec (String DecInput){
        String result;
        String zerostring = new String();

        if(DecInput.length() < 17 && DecInput.charAt(0) == '-'){
            for(int i=0;i< 17 - DecInput.length();i++){
                zerostring = "0" + zerostring;
            }

            StringBuffer reString = new StringBuffer(DecInput);
            reString.insert(1, zerostring);
            result = reString.toString();
            Globals.padded = true;
        }
        else if(DecInput.length() < 16){
            result = String.format("%16s", DecInput).replace(' ', '0');
            Globals.padded = true;
        }
        else{
            result = DecInput;
        }

        return result;
    }

    static int NormalizeExp (String DecInput, int exp){
        int result;

        if(Globals.decimalpoint != true){
            if(DecInput.length() > 16 && DecInput.charAt(0) != '-'){
                result = exp + (DecInput.length() - 16);
            }
            else if(DecInput.length() > 17 && DecInput.charAt(0) == '-'){
                result = exp + (DecInput.length() - 17);
            }
            else{
                result = exp;
            }
        }
        else{
            result = exp;
        }
        return result;
    }

    static String RoundUp(String DecInput){
        if(Globals.padded == false){
            DecInput = NormalizeDec(DecInput);
            if(DecInput.length() > 16 && DecInput.charAt(0) != '-'){
                DecInput = DecInput.substring(0, 16);
                BigInteger temp = new BigInteger(DecInput);
                BigInteger one = new BigInteger("1");
                BigInteger sum = temp.add(one);
                DecInput = sum.toString();
            }
            else if(DecInput.length() > 17 && DecInput.charAt(0) == '-'){
                DecInput = DecInput.substring(0, 17);
            }
        }
        else{
            return DecInput;
        }

        return DecInput;
    }

    static String RoundDown(String DecInput){
        if(Globals.padded == false){
            if(DecInput.length() > 16 && DecInput.charAt(0) != '-'){
                DecInput = DecInput.substring(0, 16);
            }
            else if(DecInput.length() > 17 && DecInput.charAt(0) == '-'){
                DecInput = DecInput.substring(0, 17);
                BigInteger temp = new BigInteger(DecInput);
                BigInteger one = new BigInteger("1");
                BigInteger diff = temp.subtract(one);
                DecInput = diff.toString();
            }
        }
        else{
            return DecInput;
        }
        return DecInput;
    }

    static String Truncate(String DecInput){
        if(Globals.padded == false){
            if(DecInput.length() > 16 && DecInput.charAt(0) != '-'){
                DecInput= DecInput.substring(0, 16);
            }
            else if(DecInput.length() > 17 && DecInput.charAt(0) == '-'){
                DecInput= DecInput.substring(0, 17);
            }
        }
        else{
            return DecInput;
        }

        return DecInput;
    }

    static String RoundNearest(String DecInput){
        if(Globals.padded == false){
            if(DecInput.length() > 16 && DecInput.charAt(0) != '-'){
                if(Character.getNumericValue(DecInput.charAt(16)) > 5){
                    DecInput= DecInput.substring(0, 16);
                    BigInteger temp = new BigInteger(DecInput);
                    BigInteger one = new BigInteger("1");
                    BigInteger sum = temp.add(one);
                    DecInput = sum.toString();
                }
                else if (Character.getNumericValue(DecInput.charAt(16)) == 5){
                    if(Character.getNumericValue(DecInput.charAt(15)) % 2 == 1){
                        DecInput= DecInput.substring(0, 16);
                        BigInteger temp = new BigInteger(DecInput);
                        BigInteger one = new BigInteger("1");
                        BigInteger sum = temp.add(one);
                        DecInput = sum.toString();
                    }
                    else{
                        DecInput= DecInput.substring(0, 16);
                    }
                }
                else{
                    DecInput= DecInput.substring(0, 16);
                }
            }
            else if(DecInput.length() > 17 && DecInput.charAt(0) == '-'){
                if(Character.getNumericValue(DecInput.charAt(17)) > 5){
                    DecInput= DecInput.substring(0, 17);
                    BigInteger temp = new BigInteger(DecInput);
                    BigInteger one = new BigInteger("1");
                    BigInteger sum = temp.add(one);
                    DecInput = sum.toString();
                }
                else if (Character.getNumericValue(DecInput.charAt(17)) == 5){
                    if(Character.getNumericValue(DecInput.charAt(16)) % 2 == 1){
                        DecInput= DecInput.substring(0, 17);
                        BigInteger temp = new BigInteger(DecInput);
                        BigInteger one = new BigInteger("1");
                        BigInteger sum = temp.add(one);
                        DecInput = sum.toString();
                    }
                    else{
                        DecInput= DecInput.substring(0, 17);
                    }
                }
                else{
                    DecInput= DecInput.substring(0, 17);
                }
            }
        }
        else{
            return DecInput;
        }

        return DecInput;
    }

    public static void main(String[] args){
        DecTo64 converter = new DecTo64();
    }
}