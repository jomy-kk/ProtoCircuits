package com.protocircuits.simulations;

import com.protocircuits.views.running.RunningView; // GUI view

import java.awt.Point;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.protocircuits.simulations.Block.Shape;


public class Environment {

    private final static Logger logger = Logger.getLogger(RunningView.class.getName());
    public static Random random = new Random();

    public static int prepared_simulation = 4;

    public static int nX, nY;
    public static Block[][] environment;
    public static EnvironmentEntity[][] objects;
    private static Set<Protocell> protocells;
    private static Set<LivingCell> livingCells;
    private static Set<Molecule> molecules;
    private static Set<RedBloodCell> rbcs;
    public static Map<Integer, Integer> ssDNACounters;

    public enum EnvironmentType { PETRI_DISH, BLOOD_VESSEL, GUT }
    public static EnvironmentType envType;

    /****************************
     ***** A: SETTING ENVIR *****
     ****************************/

    public static void initialize() {

        protocells = new HashSet<>();
        livingCells = new HashSet<>();
        molecules = new HashSet<>();
        rbcs = new HashSet<>();
        ssDNACounters = new HashMap<>();

        objects = new EnvironmentEntity[nX][nY]; // what is in each block position?
        environment = new Block[nX][nY];

        /* A: create 2D environment all free blocks */
        if(envType == EnvironmentType.PETRI_DISH) {
            int dish_width = nX;
            double midPoint = (dish_width-1)/2.0;
            for (int col = 0; col < dish_width; col++)
            {
                double yy = col-midPoint;
                for (int x=0; x<dish_width; x++)
                {
                    double xx = x-midPoint;
                    if (Math.sqrt(xx*xx+yy*yy) <= midPoint) {
                        environment[x][col] = new Block(Shape.FREE, new Point(x, col));
                    }

                    else
                        continue; // if is not a free block, then it is not allowed. When getting it should return null.
                }
            }
        }

        /* B: create ECM and blood vessel */
        if(envType == EnvironmentType.BLOOD_VESSEL) {
            int vessel_size = 20;
            int intimas_size = 2;
            int vesselX = 0, vesselY = 20; // bottom-left corner

            for(int x = 0; x < nX; x++) {
                for(int y = 0; y < nY; y++) {
                    if(y >= vesselY && y < vesselY + vessel_size) { // it's blood vessel
                        if (y < vesselY + intimas_size) { // it's external blood vessel wall
                            environment[x][y] = new Block(Shape.TUNICA_EXTERNA);
                        }
                        if (y >= vesselY + intimas_size && y < vesselY + 2*intimas_size) { // it's medial blood vessel wall
                            environment[x][y] = new Block(Shape.TUNICA_MEDIA);
                        }
                        if (y >= vesselY + 2*intimas_size && y < vesselY + vessel_size - 2*intimas_size) { // it's plasma
                            environment[x][y] = new Block(Shape.PLASMA);
                        }
                        if (y >= vesselY + vessel_size - 2*intimas_size && y < vesselY + vessel_size - intimas_size) { // it's medial blood vessel wall
                            environment[x][y] = new Block(Shape.TUNICA_MEDIA);
                        }
                        if (y >= vesselY + vessel_size - intimas_size) { // it's external blood vessel wall
                            environment[x][y] = new Block(Shape.TUNICA_EXTERNA);
                        }
                    }
                    else { // it's ECM
                        environment[x][y] = new Block(Shape.ECM);
                    }
                }
            }

            // TODO: add some random RBCs
            Point[] pRBCs = new Point[] {new Point(20,vesselY + 2*intimas_size + 5), };  // bottom-left corners
            Size rbcSize = new Size(4,4);

            for(int k=0; k<pRBCs.length; k++)
                for(int dx=0; dx<rbcSize.x; dx++)
                    for(int dy=0; dy<rbcSize.y; dy++) {
                        RedBloodCell rbc = new RedBloodCell(pRBCs[k]);
                        rbcs.add(rbc);
                    }

        }

        // Populate
        addAgentsToEnvironment(prepared_simulation);
    }

    /****************************
     ***** B: ENV METHODS *****
     ****************************/

    public static EnvironmentEntity getEntity(Point point) {
        if ((0 < point.x && point.x < objects.length - 1) && (0 < point.y && point.y < objects.length - 1)) // check matrix boundaries
            return objects[point.x][point.y];
        else
            return null;
    }

    public static Block getBlock(Point point) {
        if ((0 < point.x && point.x < environment.length - 1) && (0 < point.y && point.y < environment.length - 1)) // check matrix boundaries
            return environment[point.x][point.y];
        else
            return null;
    }

    public static void updateEntityPosition(Position oldPosition, EnvironmentEntity entity) {
        // remove
        removeEntity(oldPosition);
        // insert
        insertEntity(entity);
    }

    public static void removeEntity(Position position) {
        for(Point p: position.getAreaPoints()) {
            if ((0 < p.x && p.x < objects.length - 1) && (0 < p.y && p.y < objects.length - 1)) // check matrix boundaries
                objects[p.x][p.y] = null;
        }
    }

    public static void insertEntity(EnvironmentEntity entity) {
        for(Point p: entity.position.getAreaPoints()) {
            if ((0 < p.x && p.x < objects.length - 1) && (0 < p.y && p.y < objects.length - 1)) // check matrix boundaries
                objects[p.x][p.y] = entity;
        }
    }

    /***********************************
     ***** C: ELICIT AGENT ACTIONS *****
     ***********************************/

    private static RunThread runThread;
    private static RunningView GUI;


    public static void associateGUI(RunningView gui) {
        GUI = gui;
    }

    public static class RunThread extends Thread {

        int step;

        public RunThread(int step){
            this.step = step;
        }

        public void run() {
            while(!this.isInterrupted()){
                step();
                try {
                    sleep(step);
                } catch (InterruptedException e) {
                    this.interrupt();
                }
            }
        }
    }

    public static void run(int step) {
        Environment.runThread = new RunThread(step);
        Environment.runThread.start();
    }

    public static void reset() {
        initialize();
        GUI.update();
    }

    public static void step() {
        for(LivingCell agent : livingCells)
            agent.agentDecision();
        for(Protocell agent : protocells)
            agent.agentDecision();
        for(Molecule agent : molecules)
            agent.agentDecision();

        GUI.update();
    }

    public static void stop() {
        runThread.interrupt();
    }

    /****************  ADDING AGENTS  ***************/

    public static void addAgentsToEnvironment(int config) {
        switch (config) {
            case 1:
                int n_protocells = 20;
                int n_ssdnas = 100;

                for (int i = 0; i < n_protocells; ++i) {
                    int x = random.nextInt(nX), y = random.nextInt(nY);
                    GeneticCircuit circuit = new AmplifierGeneticCircuit(new SSDNA("AAC"), new SSDNA("TTGACTGTTAC", 0), 100);
                    if (environment[x][y] != null) registerAgent(new Nanorobot(new Point(x, y), 1, circuit));
                }

                for (int i = 0; i < n_ssdnas; ++i) {
                    int x = random.nextInt(nX), y = random.nextInt(nY);
                    if (environment[x][y] != null) registerAgent(new SSDNA(new Point(x, y), "TTGACTGTTAC", 0));
                }

                break;

            case 2:
                for (int i = 0; i < 10; ++i) {
                    int x = random.nextInt(nX), y = random.nextInt(nY);
                    GeneticCircuit circuit = new AmplifierGeneticCircuit(new SSDNA("AAC"), new SSDNA("TTGACTGTTAC", 0), 50);
                    if (environment[x][y] != null) registerAgent(new Nanorobot(new Point(x, y), 1, circuit));
                }

                for (int i = 0; i < 10; ++i) {
                    int x = random.nextInt(nX), y = random.nextInt(nY);
                    GeneticCircuit circuit = new TransducerGeneticCircuit(new SSDNA("AAC"), new SSDNA("CCGCTACGAGCG", 1), 100);
                    if (environment[x][y] != null) registerAgent(new Nanorobot(new Point(x, y), 1, circuit));
                }
                ssDNACounters.put(1, 0); // starts type 1 counter to 0 (to appear on the plot)

                for (int i = 0; i < 50; ++i) {
                    int x = random.nextInt(nX), y = random.nextInt(nY);
                    if (environment[x][y] != null) registerAgent(new SSDNA(new Point(x, y), "TTGACTGTTAC", 0));
                }

                break;

            case 3:

                for (int i = 0; i < 50; ++i) {
                    int x = random.nextInt(nX), y = random.nextInt(nY);
                    if (environment[x][y] != null) registerAgent(new SSDNA(new Point(x, y), "TTGACTGTTAC", 0));
                }

                for (int i = 0; i < 10; ++i) {
                    int x = random.nextInt(nX), y = random.nextInt(nY);
                    GeneticCircuit circuit = new AmplifierGeneticCircuit(new SSDNA("AAC"), new SSDNA("TTGACTGTTAC", 0), 50);
                    if (environment[x][y] != null) registerAgent(new Nanorobot(new Point(x, y), 1, circuit));
                }

                for (int i = 0; i < 10; ++i) {
                    int x = random.nextInt(nX), y = random.nextInt(nY);
                    GeneticCircuit circuit = new TransducerGeneticCircuit(new SSDNA("AAC"), new SSDNA("CCGCTACGAGCG", 1), 100);
                    if (environment[x][y] != null) registerAgent(new Nanorobot(new Point(x, y), 1, circuit));
                }
                ssDNACounters.put(1, 0); // starts type 1 counter to 0 (to appear on the plot)

                for (int i = 0; i < 10; ++i) {
                    int x = random.nextInt(nX), y = random.nextInt(nY);
                    GeneticCircuit circuit = new AndGeneticCircuit(new SSDNA("AAC"), new SSDNA("GGC"), new SSDNA("ACTCGATGTC", 2), 100);
                    if (environment[x][y] != null) registerAgent(new Nanorobot(new Point(x, y), 1, circuit));
                }
                ssDNACounters.put(2, 0); // starts type 2 counter to 0 (to appear on the plot)

                break;

            case 4:

                // tumorous
                registerAgent(new EpithelialLivingCell(new Point(30, 30), true));
                registerAgent(new EpithelialLivingCell(new Point(70, 35), true));

                // healthy
                registerAgent(new EpithelialLivingCell(new Point(25, 60), false));
                registerAgent(new EpithelialLivingCell(new Point(60, 75), false));


                for (int i = 0; i < 20; ++i) {
                    int x = random.nextInt(nX), y = random.nextInt(nY);
                    GeneticCircuit circuit = new AndGeneticCircuit(new SSDNA("GGC"), new SSDNA("TTG"), new SSDNA("TACGGCGCTATC", 1), 10);
                    if (environment[x][y] != null) registerAgent(new Nanorobot(new Point(x, y), 1, circuit));
                }

                for (int i = 0; i < 100; ++i) {
                    int x = random.nextInt(nX), y = random.nextInt(nY);
                    if (environment[x][y] != null) registerAgent(new SSDNA(new Point(x, y), "CCGCTACGAGCG", 0));
                }

                break;
        }

        /* C: Assign each entity to objects map */
        for(Protocell pc : protocells) insertEntity(pc);
        for(LivingCell lc : livingCells) insertEntity(lc);
        for(Molecule mol : molecules) insertEntity(mol);

        logger.log(Level.INFO, "Added agents");
    }


    public static void registerAgent(Molecule molecule) {
        boolean added = molecules.add(molecule); // this is a set so no repeated molecules are added
        if (added && molecule instanceof SSDNA) {
            int type = ((SSDNA) molecule).type;
            Integer counter = ssDNACounters.get(type);
            if (counter == null)
                ssDNACounters.put(type, 1); // very begin of the counter of this ssDNA counter
            else
                ssDNACounters.put(type, counter + 1); // increment counter

            // ssDNACounters.merge(type, 1, Integer::sum); // or this for more simplicity
        }
    }

    public static void unregisterAgent(Molecule molecule) {
        boolean removed = molecules.remove(molecule);
        if (removed && molecule instanceof SSDNA) {
            int type = ((SSDNA) molecule).type;
            Integer counter = ssDNACounters.get(type);
            ssDNACounters.put(type, counter - 1); // decrement counter
        }
    }

    public static void registerAgent(Protocell protocell) {
        protocells.add(protocell);
    }

    public static void registerAgent(LivingCell cell) {
        livingCells.add(cell);
    }



}



