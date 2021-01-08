/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Exe.Snippet;
import Managers.ColorManager;
import Perfomance.CodeCreator;

import javax.swing.*;
import java.awt.Color;
import java.io.*;

import static Interfaz.MyOptionPane.showMessage;

/**
 *
 * @author daniel
 */
public class CSource extends javax.swing.JDialog {

    /**
     * Creates new form LoadGrammar
     */
    public CSource(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setSize(800, 600);
        setLocationRelativeTo(null);
        jTextArea1.setCaretColor(Color.yellow);
        jTextArea1.setForeground(Color.white);
        jTextArea1.setBackground(ColorManager.ESCALA_AZUL.getMaxiumScaleColor());

    }

    private static String outPath = Snippet.propierties.rootActual + "/out";

    public boolean compileCode(CodeCreator cc) {
        //Checa si el directorio para archivos c y compilados existen
        try {
            File CFolder = new File(outPath);

            if (!CFolder.exists())
                CFolder.mkdir();

            String cCodeFile = CFolder.getPath() + "/" + cc.fileName + ".c";
            File cfile = new File(cCodeFile);

            if (cfile.exists())
                cfile.delete();

            cfile.createNewFile();

            try (FileWriter fw = new FileWriter(cfile)) {
                try (BufferedWriter bw = new BufferedWriter(fw)) {
                    bw.write(cc.getCode());
                }
            }

            if(hasGCC())
            {
                String pathOutFile = outPath + "/"+cc.fileName+".out";
                return compile(cCodeFile,pathOutFile);
            }
            return false;
        }catch(IOException ex){

            ex.printStackTrace();
            return false;
        }

    }

    public void execute(CodeCreator cc){

        if (!new File(outPath+"/"+cc.fileName+".out").exists()){
            if(!compileCode(cc))
                return;
        }

        String os = System.getProperty("os.name");

        String commands[];
        if(os.startsWith("Linux"))
            commands = new String[] {"x-terminal-emulator","-e",outPath+"/"+cc.fileName+".out"};
        else if(os.startsWith("Windows"))
            commands = new String[] {"cmd","/k","start","cmd.exe","/C",outPath+"/"+cc.fileName+".out"};
        else
        {
            showMessage(null,"No se determino " +
                            "correctamente el sistema operativo","Error al ejecutar el archivo",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        ProcessBuilder pb;
        pb = new ProcessBuilder(commands);
        try {
            pb.start();
        } catch (IOException e) {
            showMessage(null,e.getMessage(),"Error al ejecutar el archivo",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private boolean compile(String cFile,String outFile){

        ProcessBuilder pb = new ProcessBuilder("gcc",cFile,"-o",outFile);
        try {
            pb.start();
            Compilador.notify.notifyMsg("Compilando archivo, espere unos segundos..." ).run();
        } catch (IOException e) {
            showMessage(null,e.getMessage(),"Error al compilar el archivo",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean hasGCC(){

        ProcessBuilder pb = new ProcessBuilder("gcc","-v");

        try {
            Process pr = pb.start();
//            readOutput(pr.getInputStream());
//            readOutput(pr.getErrorStream());
//            pr.destroy();
        } catch (IOException  e) {
            showMessage(null,"Verifque que tenga instalado el compilador de gcc\n O intente" +
                    " ejecutando el programa desde un editor de textos o terminal." +
                            "\nError: "+e.getMessage(),
                    "Error al compilar el archivo",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private static void readOutput(InputStream input){
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input)))
        {
            String out;

            System.out.println("Reading...");
            while((out=br.readLine())!=null)
            {
                System.out.println("Out: "+out);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setOUT(String code)
    {
        jTextArea1.setText(code);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));
        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(7, 7, 7, 7));

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setTabSize(5);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
