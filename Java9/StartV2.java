import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class StartV2{

    static Object pauser = new Object();
    public static void main(String[] args) {

        GraphMaker BLD = new GraphMaker();
        BLD.createAndShowGUI();
        JButton What = BLD.addAButton("Load");
        BLD.AddButtonFunctionality(What, BLD);

    }

   
}

class GraphicsWithBoxLayout{
    
    static Object pauser = new Object();
    protected String FileName;
    protected Container cont;
    protected JFrame window;

    public JButton addAButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        cont.add(button);
        window.setSize(400, 400);
        window.setLocationRelativeTo(null);
        return button;
    }
    
    public void AddButtonFunctionality(JButton button, GraphMaker GM){
        button.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("txt files", "txt");
                chooser.setFileFilter(filter);
                int returnvalue = chooser.showOpenDialog(cont);
                if (returnvalue == JFileChooser.APPROVE_OPTION){
                    FileName = chooser.getSelectedFile().getAbsolutePath();
                    GM.OpenAndSetGraphData();
                }
            }

        });
    }

    public void createAndShowGUI() {
        window = new JFrame("No file selected");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        cont = window.getContentPane();
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
        window.pack();
        window.setVisible(true);
    }
}

class GraphMaker extends GraphicsWithBoxLayout{

    List<Position2D> GraphPoints = new ArrayList<Position2D>();
    Map<Position2D, Integer> GraphConnections = new HashMap<Position2D, Integer>();
    Panel pane = null;

    public void OpenAndSetGraphData(){
        GraphPoints.clear();
        GraphConnections.clear();
        try{
            File myFile = new File(FileName);
            Scanner myReader = new Scanner(myFile);
            if(myReader.hasNextInt()){
                int i = myReader.nextInt();
                while (i != 0){
                    GraphPoints.add(new Position2D(myReader.nextInt(), myReader.nextInt()));
                    i--;
                }
                if (myReader.hasNextInt()){
                    i = myReader.nextInt();
                    while (i != 0){
                        GraphConnections.put(new Position2D(myReader.nextInt(), myReader.nextInt()), myReader.nextInt());
                        i--;
                    }
                }
            }
            if (CheckIfNegative(GraphPoints)){
                GraphPoints = NegativeToPositive(GraphPoints);
            }
            window.setTitle(myFile.getName());
            DrawGraph();
            myReader.close();
        }
        catch(FileNotFoundException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void DrawGraph(){
        window.setSize(500, 500);
        window.setLocationRelativeTo(null);
        if (pane == null){
            pane = new Panel(GraphPoints, GraphConnections);
            cont.add(pane);
        }
        else {
            pane.Reset(GraphPoints, GraphConnections);
        }
        window.repaint();

    }
    
    private boolean CheckIfNegative(List<Position2D> GPs){
        for (int i = 0; i < GPs.size(); i++){
            if (GPs.get(i).getCol() < 0){
                return true;
            }
            if (GPs.get(i).getRow() < 0){
                return true;
            }
        }
        return false;
    }

    private List<Position2D> NegativeToPositive (List<Position2D> GPs){
        Integer minCol = 0;
        Integer minRow = 0;

        List<Position2D> Result = new ArrayList<Position2D>();

        for (int i = 0; i < GPs.size(); i++){
            if (GPs.get(i).getCol() < minCol){
                minCol = GPs.get(i).getCol();
            }
            if (GPs.get(i).getRow() < minRow){
                minRow = GPs.get(i).getRow();
            }
        }

        for (int i = 0; i < GPs.size(); i++){
            Position2D Pos = new Position2D(GPs.get(i).getCol() + Math.abs(minCol), GPs.get(i).getRow() + Math.abs(minRow));
            Result.add(Pos);
        }

        return Result;
    }

}


class Panel extends JPanel{
    
    List<Position2D> GraphPoints = new ArrayList<Position2D>();
    Map<Position2D, Integer> GraphConnections = new HashMap<Position2D, Integer>();
    Double Wspolczynnikx = 0.0;
    Double Wspolczynniky = 0.0;
    Double Marginesx = 0.0;
    Double Marginesy = 0.0;
    Integer WspolczynnikKola = 0;
    
    public Panel(List<Position2D> GP, Map<Position2D, Integer> GC){
        GraphPoints = GP;
        GraphConnections = GC;
        this.revalidate();
        this.repaint();
    }

    public void Reset(List<Position2D> GP, Map<Position2D, Integer> GC){
        GraphPoints = GP;
        GraphConnections = GC;
        Wspolczynnikx = 0.0;
        Wspolczynniky = 0.0;
        Marginesx = 0.0;
        Marginesy = 0.0;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Integer GraphWidth = SearchGraphWidth();
        Integer GraphHeight = SearchGraphHeight();
        Integer MinimalWidth = SearchMinimalWidth();
        Integer MinimalHeight = SearchMinimalHeight();
        if (GraphHeight == 0 && GraphWidth == 0){
            Wspolczynnikx = 0.0;
            Wspolczynniky = 0.0;
            if (getWidth() > getHeight()){
                WspolczynnikKola = (getHeight() - 2);
            }
            else{
                WspolczynnikKola = (getWidth() - 2);
            }
        }
        else{
            Wspolczynnikx = FindWspolczynnik();
            Wspolczynniky = FindWspolczynnik();
            WspolczynnikKola = 10;
        }   
        Marginesx = (double) ((getWidth() - GraphWidth * Wspolczynnikx - (double)WspolczynnikKola * ScaleWspolczynnik()) / 2.0);
        Marginesy = (double) ((getHeight() - GraphHeight * Wspolczynniky + (double)WspolczynnikKola * ScaleWspolczynnik()) / 2.0);
        
        if (GraphHeight == 0 && GraphWidth == 0){
            if (getWidth() > getHeight()){
                Marginesx = ((double)getWidth() - (double)WspolczynnikKola) / 2.0;
                Marginesy = (double) getHeight();
            }
            else if(getWidth() == getHeight()){
                Marginesx = 0.0;
                Marginesy = (double) getHeight();
            }
            else{
                Marginesx = 0.0;
                Marginesy = (double) getHeight() - (((double)getHeight() - (double)WspolczynnikKola) / 2.0);
            }
            Wspolczynnikx = 1.0;
            Wspolczynniky = 1.0;
        }
        
        for (var i : GraphConnections.keySet()){
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setStroke(new BasicStroke((float) searchMaxInMap(i) * (float)((double)ScaleWspolczynnik())));
            g2d.drawLine((int) (Marginesx + GraphPoints.get(i.getCol()-1).getCol() * Wspolczynnikx + 5.0 * ScaleWspolczynnik() - MinimalWidth * Wspolczynnikx), (int) (getHeight() - GraphPoints.get(i.getCol() - 1).getRow() * Wspolczynniky + MinimalHeight * Wspolczynniky + 5.0 * ScaleWspolczynnik() - Marginesy), (int) (Marginesx + GraphPoints.get(i.getRow() - 1).getCol() * Wspolczynnikx + 5.0 * ScaleWspolczynnik() - MinimalWidth * Wspolczynnikx), (int) (getHeight() - GraphPoints.get(i.getRow()-1).getRow() * Wspolczynniky + MinimalHeight * Wspolczynniky + 5.0 * ScaleWspolczynnik() - Marginesy));
            g2d.dispose();
        }
        for (int i = 0; i < GraphPoints.size(); i++){
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(Color.WHITE);
            g2d.fillOval((int) (Marginesx + GraphPoints.get(i).getCol() * Wspolczynnikx - MinimalWidth * Wspolczynnikx - ScaleWspolczynnik()), (int) (getHeight() - GraphPoints.get(i).getRow() * Wspolczynniky + MinimalHeight * Wspolczynniky - Marginesy - ScaleWspolczynnik()), (int)((double)WspolczynnikKola*ScaleWspolczynnik()), (int)((double)WspolczynnikKola*ScaleWspolczynnik()));
            g2d.setColor(Color.BLACK);
            g2d.drawOval((int) (Marginesx + GraphPoints.get(i).getCol() * Wspolczynnikx - MinimalWidth * Wspolczynnikx - ScaleWspolczynnik()), (int) (getHeight() - GraphPoints.get(i).getRow() * Wspolczynniky + MinimalHeight * Wspolczynniky - Marginesy - ScaleWspolczynnik()), (int)((double)WspolczynnikKola*ScaleWspolczynnik()), (int)((double)WspolczynnikKola*ScaleWspolczynnik()));
            g2d.dispose();
        }

    }

    private Double ScaleWspolczynnik() {
        if (SearchGraphHeight() == 0 && SearchGraphWidth() == 0){
            return 1.0;
        }
        if (SearchGraphHeight() < 10 && SearchGraphWidth() < 10){
            Double KopiaWspolczynnik;
            if (SearchGraphHeight() > SearchGraphWidth()){
                KopiaWspolczynnik = Wspolczynnikx / (double)SearchGraphHeight();
            }
            else {
                KopiaWspolczynnik = Wspolczynnikx / (double)SearchGraphWidth();
            }
            if (KopiaWspolczynnik > 20){
                return 2.0;
            }
            else{
                return KopiaWspolczynnik / 10.0;
            }
        }
        else{
            if (Wspolczynnikx > 20){
                return 2.0;
            }
            else{
                return Wspolczynnikx/10.0;
            }
        }
    }

    private Double FindWspolczynnik() {
        Double WspolczynnikHeight = 1.0;
        Double WspolczynnikWidth = 1.0;
        if (SearchGraphHeight() != 0){
            for (WspolczynnikHeight = 1.0; WspolczynnikHeight * (double) SearchGraphHeight() < ((double)getHeight() - 30.0); WspolczynnikHeight += 1.0);
            WspolczynnikHeight -= 1.0;
        }
        if (SearchGraphWidth() != 0){
            for (WspolczynnikWidth = 1.0; WspolczynnikWidth * (double) SearchGraphWidth() < ((double)getWidth() - 30.0); WspolczynnikWidth += 1.0);
            WspolczynnikWidth -= 1.0;
        }
        if (SearchGraphHeight() == 0 || SearchGraphWidth() == 0){
            if (WspolczynnikHeight > WspolczynnikWidth){
                WspolczynnikWidth = WspolczynnikHeight;
            }
            else {
                WspolczynnikHeight = WspolczynnikWidth;
            }
        }
        return WspolczynnikHeight > WspolczynnikWidth ? WspolczynnikWidth : WspolczynnikHeight;
    }

    private float searchMaxInMap(Position2D a) {
        float max = 0;
        for (var i : GraphConnections.values()){
            if((float) i > max){
                max = (float) i;
            }
        }
        return (((float)GraphConnections.get(a)/max) * 4);
    }

    private Integer SearchMinimalHeight() {
        int min;
        if (GraphPoints.size() == 0){
            min = 0;
        }
        else{
            min = GraphPoints.get(0).getRow();
        }
        for (int i = 0; i < GraphPoints.size(); i++){
            if(GraphPoints.get(i).getRow() < min){
                min = GraphPoints.get(i).getRow();
            }
        }
        
        return min;
    }

    private Integer SearchMinimalWidth() {
        int min;
        if (GraphPoints.size() == 0){
            min = 0;
        }
        else{
            min = GraphPoints.get(0).getCol();
        }
        for (int i = 0; i < GraphPoints.size(); i++){
            if(GraphPoints.get(i).getCol() < min){
                min = GraphPoints.get(i).getCol();
            }
        }
        
        return min;
    }

    private Integer SearchGraphHeight() {
        int max;
        int min;
        if (GraphPoints.size() == 0){
            min = 0;
            max = 0;
        }
        else{
            max = GraphPoints.get(0).getRow();
            min = GraphPoints.get(0).getRow();
        }
        for (int i = 0; i < GraphPoints.size(); i++){
            if(GraphPoints.get(i).getRow() > max){
                max = GraphPoints.get(i).getRow();
            }
            if(GraphPoints.get(i).getRow() < min){
                min = GraphPoints.get(i).getRow();
            }
        }
        
        return (max - min);
    }

    private Integer SearchGraphWidth() {
        int max;
        int min;
        if (GraphPoints.size() == 0){
            min = 0;
            max = 0;
        }
        else{
            max = GraphPoints.get(0).getCol();
            min = GraphPoints.get(0).getCol();
        }
        for (int i = 0; i < GraphPoints.size(); i++){
            if(GraphPoints.get(i).getCol() > max){
                max = GraphPoints.get(i).getCol();
            }
            if(GraphPoints.get(i).getCol() < min){
                min = GraphPoints.get(i).getCol();
            }
        }
        
        return (max - min);
    }

}

class Position2D{
	
	final private int row;
	final private int col;
	
	public Position2D(int col, int row ) {
		this.row = row;
		this.col = col;
	}

	@Override
	public String toString() {
		return "Position2D [col=" + col + ", row =" + row + "]";
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	@Override
	public int hashCode() {
//		System.out.println( "hashCode dla " + this );
		return Objects.hash(col, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position2D other = (Position2D) obj;
//		System.out.println( "equals " + this + " vs " + other );
		return col == other.col && row == other.row;
	}	
}
