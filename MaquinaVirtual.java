package maquinavirtual;

import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public class MaquinaVirtual extends JFrame {
	
    private  JButton abrir;
    private  JButton rodar;
    private  JButton debug;
    private  JButton parar;
    private  JTable programa;
    private  JTable pilha;
    private  JTextArea entrada;
    private  JTextArea saida;
    private  JTextArea console;
    private  JLabel proglab;
    private  JLabel pilhalab;
    private  JLabel entralab;
    private  JLabel sailab;
    private  JLabel conlab;
    private  JScrollPane paneprog;
    private  JScrollPane panepilha;
    private  JScrollPane posentrada;
    private  JScrollPane possaida;
    private  JScrollPane poscon;
    private  int s;
    private  Object[] stack = new Object[100];
    private  int steps=0;
    private File arqaux;
    
    public MaquinaVirtual() {
        
        JFrame vm = new JFrame("Maquina Virtual");
        vm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vm.setSize(710,630);
        vm.setResizable(false);
        vm.setVisible(true);
        vm.setLayout(null);
        
        
        abrir = new JButton ("OPEN");
        abrir.setBounds(20, 10, 100, 30);
        vm.add(abrir);
        
        rodar = new JButton ("RUN");
        rodar.setBounds(190, 10, 100, 30);
        vm.add(rodar);
        
        debug = new JButton ("DEBUG");
        debug.setBounds(360, 10, 100, 30);
        vm.add(debug);
        
        parar = new JButton ("STOP");
        parar.setBounds(530, 10, 100, 30);
        vm.add(parar);
        
        proglab = new JLabel("Programa");
        proglab.setBounds(130,60,80, 20);
        proglab.setForeground(Color.BLACK);
	vm.add(proglab);
        
        pilhalab = new JLabel("Pilha");
        pilhalab.setBounds(410,60,80, 20);
        pilhalab.setForeground(Color.BLACK);
	vm.add(pilhalab);
                
        entralab = new JLabel("Entrada");
        entralab.setBounds(580,60,80, 20);
        entralab.setForeground(Color.BLACK);
	vm.add(entralab);
        
        sailab = new JLabel("Saída");
        sailab.setBounds(580,240,80, 20);
        sailab.setForeground(Color.BLACK);
	vm.add(sailab);
        
        sailab = new JLabel("Console");
        sailab.setBounds(580,420,80, 20);
        sailab.setForeground(Color.BLACK);
	vm.add(sailab);
        
        
        entrada = new JTextArea();
        entrada.setEditable(false);
        entrada.setBorder(BorderFactory.createLineBorder(new Color(112,128,144)));
        
        saida = new JTextArea();
        saida.setEditable(false);
        saida.setBorder(BorderFactory.createLineBorder(new Color(112,128,144)));
        
        console = new JTextArea();
        console.setEditable(false);
        console.setBorder(BorderFactory.createLineBorder(new Color(112,128,144)));
        
        programa = new JTable();
        
        programa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Linha", "Instrução", "Atributo 1", "Atributo 2"})
                
            {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        
        programa.getColumnModel().getColumn(0).setPreferredWidth(50);
        programa.getColumnModel().getColumn(1).setPreferredWidth(90);
        programa.getColumnModel().getColumn(2).setPreferredWidth(70);
        programa.getColumnModel().getColumn(3).setPreferredWidth(70);
        programa.getTableHeader().setResizingAllowed(false);
        programa.setAutoResizeMode(programa.AUTO_RESIZE_OFF);
        programa.getTableHeader().setReorderingAllowed(false);  
        paneprog = new JScrollPane(programa);
        paneprog.setBounds(20,80,300,500);
        programa.setPreferredScrollableViewportSize(programa.getPreferredSize());
        programa.setFillsViewportHeight(true);   
        vm.add(paneprog);
        
        
        pilha = new JTable();
        pilha.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Endereço", "Valor"})
            {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        
        pilha.getTableHeader().setResizingAllowed(false);
        pilha.getTableHeader().setReorderingAllowed(false);  
        panepilha = new JScrollPane(pilha);
        panepilha.setBounds(350,80,150,500);
        pilha.setPreferredScrollableViewportSize(pilha.getPreferredSize());
        pilha.setFillsViewportHeight(true);
        vm.add(panepilha);
        
        
        posentrada = new JScrollPane(entrada);
        posentrada.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        posentrada.setBounds(530, 80, 150, 150);
        vm.add(posentrada); 
        
        possaida = new JScrollPane(saida);
        possaida.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        possaida.setBounds(530, 260, 150, 150);
        vm.add(possaida);
        
        poscon = new JScrollPane(console);
        poscon.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        poscon.setBounds(530, 440, 150, 140);
        vm.add(poscon);
        
        AbrirArquivo actabrir = new AbrirArquivo();
        abrir.addActionListener(actabrir);
        
        rodarPrograma actrodar = new rodarPrograma();
        rodar.addActionListener(actrodar);
        
        debugarPrograma actdebug = new debugarPrograma();
        debug.addActionListener(actdebug);
        
        stopPrograma actstop = new stopPrograma();
        parar.addActionListener(actstop);
    }
    
    public void pintaLinha() throws IOException {
        
            programa.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){

            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
            JLabel lab = (JLabel) super.getTableCellRendererComponent(jtable, o, bln, bln1, i, i1); 

            Color c = new Color(220,220,220);
            Object obj = jtable.getValueAt(i, 0);
            int n = Integer.parseInt(obj.toString());

            if(n==steps)
            {
                c = new Color(220,220,220);
            }
            else{
                c = Color.WHITE;
            }

            lab.setBackground(c);
            return lab;
            }
        });
    }
    
    public void attArq() throws IOException{
        try{
        FileReader arq = new FileReader(arqaux);
        BufferedReader lerArq = new BufferedReader(arq);      
        DefaultTableModel modtab = (DefaultTableModel) programa.getModel();
        modtab.setNumRows(0);        
        String linha = lerArq.readLine();
        String[] sep = linha.split(" ");
        
        int tam = sep.length;
        int pos = 1;
        
        if(tam==1){
            Object[] dados = {pos, sep[0], "", ""};
            modtab.addRow(dados);
        }
        pos++;

        while((linha=lerArq.readLine())!=null)
          { 
            linha = linha.replaceAll(","," ");
            sep = linha.split(" ");
            tam = sep.length;
            if(tam==1){
                Object[] dados = {pos, sep[0], "", ""};
                modtab.addRow(dados);
            }
            if(tam==2){
                Object[] dados = {pos, sep[0], sep[1], ""};
                modtab.addRow(dados);
            }
            if(tam==3){
                Object[] dados = {pos, sep[0], sep[1], sep[2]};
                modtab.addRow(dados);
            }
            pos++;
          }
        pintaLinha();
        lerArq.close(); 
        } 
        catch(NullPointerException e)
        {
            System.out.print("Please, open a file first.\n");
        }
    }

    private class AbrirArquivo implements ActionListener {
        
        public void actionPerformed(ActionEvent event){
            int pos=1, tam, trows;
            String[] sep;
           
            try {
                JFileChooser file = new JFileChooser("E:\\"); 
                file.setPreferredSize(new Dimension(600, 450));
                file.setFileSelectionMode(JFileChooser.FILES_ONLY);
                file.showOpenDialog(null);
                File arquivo = file.getSelectedFile();
                arqaux = arquivo;
                int posicao = -1;
                FileReader arq = new FileReader(arquivo);
                BufferedReader lerArq = new BufferedReader(arq);
                DefaultTableModel modtab = (DefaultTableModel) programa.getModel();
                modtab.setNumRows(0);
                DefaultTableModel modtab2 = (DefaultTableModel) pilha.getModel();
                modtab2.setNumRows(0);
                steps = 0;
                trows = modtab.getRowCount();
                if(trows != 0){
                    for(int d = 0; d < trows; d++){
                        modtab.removeRow(0); 
                    }
                }
                
                String linha = lerArq.readLine();
                sep = linha.split(" ");
                tam = sep.length;
                
                if(tam==1){
                    Object[] dados = {pos, sep[0], "", ""};
                    modtab.addRow(dados);
                }
                pos++;
                
                while((linha=lerArq.readLine())!=null)
                  { 
                    linha = linha.replaceAll(","," ");
                    sep = linha.split(" ");
                    tam = sep.length;
                    if(tam==1){
                        Object[] dados = {pos, sep[0], "", ""};
                        modtab.addRow(dados);
                    }
                    if(tam==2){
                        Object[] dados = {pos, sep[0], sep[1], ""};
                        modtab.addRow(dados);
                    }
                    if(tam==3){
                        Object[] dados = {pos, sep[0], sep[1], sep[2]};
                        modtab.addRow(dados);
                    }
                    pos++;
                  }
                lerArq.close();  
            }
            catch (IOException e) {
                System.err.printf("Erro na abertura do arquivo: %s.\n",e.getMessage());
            }
            
        }
    }
    
    
    private class rodarPrograma implements ActionListener {
        
        public void actionPerformed(ActionEvent event){
            int nlin, i=0;
            Object comand, comandaux, atr1, atr2;
            String com, at1, at2;
            DefaultTableModel modtab = (DefaultTableModel) programa.getModel();
            nlin = modtab.getRowCount();
            
            while(i<nlin){
                comand = modtab.getValueAt(i, 1);
                atr1 = modtab.getValueAt(i, 2);
                atr2 = modtab.getValueAt(i, 3);
                com = comand.toString();
                at1 = atr1.toString();
                at2 = atr2.toString();
                
                if(!com.equals("JMP") && !com.equals("JMPF") && !com.equals("CALL") && !com.equals("RETURN")){
                    executaComando(com, at1, at2);
                }
                else{
                    if(com.equals("JMP"))
                    {
                        for(int j=0;j<nlin;j++){
                            comandaux = modtab.getValueAt(j, 1);
                            if(comandaux.equals(atr1)){
                                i=j;
                            }
                        }
                    }
                    
                    if(com.equals("JMPF"))
                    {
                        String n1;
                        n1 = (String) stack[s];
                        
                        if(n1.equals("0")){
                            for(int j=0;j<nlin;j++){
                            comandaux = modtab.getValueAt(j, 1);
                                if(comandaux.equals(atr1)){
                                    i=j;
                                }
                            }
                        }
                        s=s-1;  
                    }
                    
                    if(com.equals("CALL")){
                        s=s+1;
                        stack[s] = String.valueOf(i + 1);
                        for(int j=0;j<nlin;j++){
                        comandaux = modtab.getValueAt(j, 1);
                            if(comandaux.equals(atr1)){
                                i=j;
                            }
                        }
                    }
                    
                    if(com.equals("RETURN")){
                        String n1;
                        n1 = (String) stack[s];        
                        i = Integer.parseInt(n1)-1;
                        s=s-1;
                    }
                }
                
              i++;
            }
        }
    }
    
    
    private class debugarPrograma implements ActionListener {
        
        public void actionPerformed(ActionEvent event){
            int nlin;
            Object comand, comandaux, atr1, atr2=0;
            String com, at1, at2;
            DefaultTableModel modtab = (DefaultTableModel) programa.getModel();
            nlin = modtab.getRowCount();
            try {
                    attArq();
                } catch (IOException ex) {
                    Logger.getLogger(MaquinaVirtual.class.getName()).log(Level.SEVERE, null, ex);
                }
            if(steps<nlin){
                comand = modtab.getValueAt(steps, 1);
                atr1 = modtab.getValueAt(steps, 2);
                atr2 = modtab.getValueAt(steps, 3);
                com = comand.toString();
                at1 = atr1.toString();
                at2 = atr2.toString();
                
                if(!com.equals("JMP") && !com.equals("JMPF") && !com.equals("CALL") && !com.equals("RETURN")){
                    executaComando(com, at1, at2);
                }
                else{
                    if(com.equals("JMP"))
                    {
                        for(int j=0;j<nlin;j++){
                            comandaux = modtab.getValueAt(j, 1);
                            if(comandaux.equals(atr1)){
                                steps=j;
                            }
                        }
                    }
                    
                    if(com.equals("JMPF"))
                    {
                        String n1;
                        n1 = (String) stack[s];
                        
                        if(n1.equals("0")){
                            for(int j=0;j<nlin;j++){
                            comandaux = modtab.getValueAt(j, 1);
                                if(comandaux.equals(atr1)){
                                    steps=j;
                                }
                            }
                        }
                        s=s-1;  
                    }
                    
                    if(com.equals("CALL")){
                        s=s+1;
                        stack[s] = String.valueOf(steps + 1);
                        for(int j=0;j<nlin;j++){
                        comandaux = modtab.getValueAt(j, 1);
                            if(comandaux.equals(atr1)){
                                steps=j;
                            }
                        }
                    }
                    
                    if(com.equals("RETURN")){
                        String n1;
                        n1 = (String) stack[s];        
                        steps = Integer.parseInt(n1)-1;
                        s=s-1;
                    }
                } 
              steps++;
              
            }
        }
    }
    
    private class stopPrograma implements ActionListener {

        public void actionPerformed(ActionEvent event){
            s = -1;
            steps = 0;
            arqaux = null;
            entrada.setText("");
            saida.setText("");
            console.setText("");
            DefaultTableModel modtab = (DefaultTableModel) programa.getModel();
            modtab.setNumRows(0);
            DefaultTableModel modtab2 = (DefaultTableModel) pilha.getModel();
            modtab2.setNumRows(0);
        }
    }
    
    private void executaComando(String comando, String atr1, String atr2) {
        String n1, n2;
        int aux=0;
        switch (comando){
            case "LDC":
                s=s+1;
                stack[s] = atr1;
                break;
                
            case "LDV":
                s=s+1;
                stack[s] = stack[Integer.parseInt(atr1)];
                break;
                    
            case "ADD":
                n1 = (String) stack[s-1];
                n2 = (String) stack[s];
                stack[s-1] = String.valueOf(Integer.parseInt(n1) + Integer.parseInt(n2));
                s=s-1;
                break;
                    
            case "SUB":
                n1 = (String) stack[s-1];
                n2 = (String) stack[s];
                stack[s-1] = String.valueOf(Integer.parseInt(n1) - Integer.parseInt(n2));
                s=s-1;
                break;
                    
            case "MULT":
                n1 = (String) stack[s-1];
                n2 = (String) stack[s];
                stack[s-1] = String.valueOf(Integer.parseInt(n1) * Integer.parseInt(n2));
                s=s-1;
                break;
                    
            case "DIVI":
                n1 = (String) stack[s-1];
                n2 = (String) stack[s];
                stack[s-1] = String.valueOf(Integer.parseInt(n1) / Integer.parseInt(n2));
                s=s-1;
                break;
                    
            case "INV":
                n1 = (String) stack[s];
                stack[s] = String.valueOf(Integer.parseInt(n1) * -1);
                break;
                    
            case "AND":
                n1 = (String) stack[s-1];
                n2 = (String) stack[s];
                if(Integer.parseInt(n1)==1 && Integer.parseInt(n2)==1){
                    stack[s-1] = String.valueOf(1);
                }
                else{
                    stack[s-1] = String.valueOf(0);
                }
                s=s-1;
                break;
                    
            case "OR":
                n1 = (String) stack[s-1];
                n2 = (String) stack[s];
                if(Integer.parseInt(n1)==1 || Integer.parseInt(n2)==1){
                    stack[s-1] = String.valueOf(1);
                }
                else{
                    stack[s-1] = String.valueOf(0);
                }
                s=s-1;
                break;
                    
            case "NEG":
                n1 = (String) stack[s];
                stack[s] = String.valueOf(1 - Integer.parseInt(n1));
                break;
                    
            case "CME":
                n1 = (String) stack[s-1];
                n2 = (String) stack[s];
                if(Integer.parseInt(n1) < Integer.parseInt(n2)){
                    stack[s-1] = String.valueOf(1);
                }
                else{
                    stack[s-1] = String.valueOf(0);
                }
                s=s-1;
                break;
                    
            case "CMA":
                n1 = (String) stack[s-1];
                n2 = (String) stack[s];
                if(Integer.parseInt(n1) > Integer.parseInt(n2)){
                    stack[s-1] = String.valueOf(1);
                }
                else{
                    stack[s-1] = String.valueOf(0);
                }
                s=s-1;
                break;
                    
            case "CEQ":
                n1 = (String) stack[s-1];
                n2 = (String) stack[s];
                if(Integer.parseInt(n1) == Integer.parseInt(n2)){
                    stack[s-1] = String.valueOf(1);
                }
                else{
                    stack[s-1] = String.valueOf(0);
                }
                s=s-1;
                break;
                    
            case "CDIF":
                n1 = (String) stack[s-1];
                n2 = (String) stack[s];
                if(Integer.parseInt(n1) != Integer.parseInt(n2)){
                    stack[s-1] = String.valueOf(1);
                }
                else{
                    stack[s-1] = String.valueOf(0);
                }
                s=s-1;
                break;
                    
            case "CMEQ":
                n1 = (String) stack[s-1];
                n2 = (String) stack[s];
                if(Integer.parseInt(n1) <= Integer.parseInt(n2)){
                    stack[s-1] = String.valueOf(1);
                }
                else{
                    stack[s-1] = String.valueOf(0);
                }
                s=s-1;
                break;
                    
            case "CMAQ":
                n1 = (String) stack[s-1];
                n2 = (String) stack[s];
                if(Integer.parseInt(n1) >= Integer.parseInt(n2)){
                    stack[s-1] = String.valueOf(1);
                }
                else{
                    stack[s-1] = String.valueOf(0);
                }
                s=s-1;
                break;
                    
            case "START":
                s=-1;
                break;
                    
            case "HLT":
                if(console.getText().length()<1){
                    console.setText("Fim da execução.");
                }
                else
                {
                    console.setText(console.getText() + "\nFim da execução.");                    
                }
                break;
                    
            case "STR":
                stack[Integer.parseInt(atr1)] = stack[s];
                s=s-1;
                break;
                    
            case "JMP":
                break;
                    
            case "JMPF":
                break;
                    
            case "NULL":
                break;
                    
            case "RD":
                s=s+1;
                n1="";
                try{
                    n1 = JOptionPane.showInputDialog("Valor de entrada:");
                    aux = Integer.parseInt(n1);
                }
                catch (NumberFormatException e) {
                    console.setText(console.getText() + "Somente inteiros.");
		}
                if(entrada.getText().length()<1){
                    entrada.setText(n1);
                }
                else{
                    entrada.setText(entrada.getText() + "\n" + n1);
                }
                stack[s] = String.valueOf(aux);
                try {
                    attArq();
                } catch (IOException ex) {
                    Logger.getLogger(MaquinaVirtual.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
                    
            case "PRN":
                if(saida.getText().length()<1){
                    n1 = (String) stack[s];
                    saida.setText(n1);
                }
                else{
                    n1 = (String) stack[s];
                    saida.setText(saida.getText() + "\n" + n1);
                }
                s=s-1;
                break;
                    
            case "ALLOC":
                aux = Integer.parseInt(atr2);
                for(int k=0; k<aux;k++){
                    s=s+1;
                    stack[s] = stack[Integer.parseInt(atr1) + k];
                }
                break;
                    
            case "DALLOC":
                aux = Integer.parseInt(atr2)-1;
                for(int k=aux; k>=0;k--){
                    stack[Integer.parseInt(atr1) + k] = stack[s];
                    s=s-1;
                }
                break;
                    
            case "CALL":
                break;
                    
            case "RETURN":
                break;
            
            default:
                break;
        }
        atualizaTabPilha();
    }
    
    private void atualizaTabPilha(){
        
        DefaultTableModel modpil = (DefaultTableModel) pilha.getModel();
        int trows;
        
        trows = modpil.getRowCount();
        if(trows != 0){
            for(int d = 0; d < trows; d++){
                modpil.removeRow(0); 
            }
        }
        
        for(int i=0;i<=s;i++){
            Object[] dados = {i, stack[i]};
            modpil.addRow(dados);
        }
    }
 
}
