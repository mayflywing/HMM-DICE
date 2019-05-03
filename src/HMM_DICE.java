public class HMM_DICE {
    private double[] initialPick;
    private double[][] transitionProb;
    private double[][] emissionProb;
    private int[] ObservationSeq;

    public HMM_DICE(){
        this.initialPick = new double[] {1.0 / 3, 1.0 / 3, 1.0 / 3};
    }

    public void setInitialPick(double[] initialPick) {
        this.initialPick = initialPick;
    }

    public double[] getInitialPick() {
        return initialPick;
    }

    public void setTransitionProb(double[][] transitionProb) {
        this.transitionProb = transitionProb;
    }

    public double[][] getTransitionProb() {
        return transitionProb;
    }

    public void setEmissionProb(double[][] emissionProb) {
        this.emissionProb = emissionProb;
    }

    public double[][] getEmissionProb() {
        return emissionProb;
    }

    public void setObservationSeq(int[] observationSeq) {
        ObservationSeq = observationSeq;
    }

    public int[] getObservationSeq() {
        return ObservationSeq;
    }

    public void printMatrix(double[][] matrix){
        for (int i = 0; i <matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j]+"\t");
            }
            System.out.println();
        }
    }
    public void printMatrix(int[][] matrix){
        for (int i = 0; i <matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j]+"\t");
            }
            System.out.println();
        }
    }
    public void printSeq(int[] seq){
        for (int i = 0; i < seq.length; i++) {
            int dice = seq[i]+1; //values in seq[] start from 0 but dice's index start from 1
            System.out.print("D"+dice+"\t");
            if((i+1)%10==0){
                //10 numbers in one row
                System.out.println();
            }
        }
    }

    public int getMaxProDice(double[] pro){
        int dice = -1;
        double MaxProb = 0;
        for (int i = 0; i < pro.length; i++) {
            if (pro[i] > MaxProb) {
                MaxProb = pro[i];
                dice = i;
            }
        }
        System.out.println("sequence probability: "+MaxProb);
        return  dice;
    }

    public void evaluate(int[] ObservationSeq) {
        if (ObservationSeq.length ==0) {
            System.out.println("No input!");
            return;
        }

        int[] dieSeq = new int[ObservationSeq.length];
        double[][] PathProbability = new double[ObservationSeq.length][transitionProb.length];
        int[][] Parent = new int[ObservationSeq.length][transitionProb.length];


        //t = 0
        PathProbability[0][0] = emissionProb[0][ObservationSeq[0]-1] * initialPick[0]; //die 1 shows ObservationSeq[0]
        PathProbability[0][1] = emissionProb[1][ObservationSeq[0]-1] * initialPick[1]; //die 2 shows ObservationSeq[0]
        PathProbability[0][2] = emissionProb[2][ObservationSeq[0]-1] * initialPick[2]; //die 3 shows ObservationSeq[0]


        for (int t = 1; t < ObservationSeq.length; t++) {
            // t = i
            for (int j = 0; j < transitionProb.length; j++) {
                //for each t, state j
                int dice = -1;
                double maxProb = 0;
                for (int i = 0; i < transitionProb.length; i++) {
                    double temp = PathProbability[t-1][i] *  //for each t-1 state die-i
                            transitionProb[i][j] *   //transition from die-i to die-j
                            emissionProb[j][ObservationSeq[t]-1];// die j shows ObservationSeq[t]
                    if (temp > maxProb) {
                        maxProb = temp;
                        dice = i;
                    }
                }
                // time t时,对每个状态j,
                // 求在time t-1 时状态为i,观测为ObservationSeq[t-1],并且在time t时状态为j观测为ObservationSeq[t]的路径的最大概率
                //同时，对每个状态j，记录令路径概率最大的前一个状态i
                PathProbability[t][j]   = maxProb;
                Parent[t][j] = dice; //time t ,state = j, most possible time t-1 state = dice;
            }
        }

        //printMatrix(PathProbability);
        //printMatrix(possibleSeq);

        dieSeq[dieSeq.length-1] = getMaxProDice(PathProbability[dieSeq.length-1]);
        //max (proMatrix[end][]) The end node of the optimal path, taking the state with the highest probability in the end.

        for (int t = dieSeq.length-2; t>= 0; t--) {
            //trace back this path
            dieSeq[t] = Parent[t+1][dieSeq[t+1]];
        }


        System.out.println("Most possible dies sequence: ");
        printSeq(dieSeq);
    }

    public static void main (String[] args) {
        HMM_DICE hmm_dice = new HMM_DICE();
        //for (int i = 0; i < 10; i++) {
            //String filename = "InputFiles/InputFile"+i+".txt";
        String filename = "InputFiles/InputFile8.txt";
        System.out.println(filename);
        fileReader.readFileByLines(filename,hmm_dice);
        hmm_dice.evaluate(hmm_dice.getObservationSeq());
        //}
    }

    public static void test1(){
        HMM_DICE hmm_dice = new HMM_DICE();
        //hmm_dice.printMatrix(hmm_dice.getTransitionProb());
        //hmm_dice.printMatrix(hmm_dice.getEmissionProb());
        //hmm_dice.printSeq(hmm_dice.getObservationSeq());
        double[] initialPick = new double[] {0.2, 0.4, 0.4};
        double[][] transitionProb = new double[][]{
                {0.5,0.2,0.3},
                {0.3,0.5,0.2},
                {0.2,0.3,0.5}
        };
        double[][] emissionProb = new double[][]{
                {0.5,0.5},
                {0.4,0.6},
                {0.7,0.3}
        };
        int[] ObservationSeq = new int[]{1,2,1};

        hmm_dice.setInitialPick(initialPick);
        hmm_dice.setTransitionProb(transitionProb);
        hmm_dice.setEmissionProb(emissionProb);
        hmm_dice.setObservationSeq(ObservationSeq);
        hmm_dice.evaluate(hmm_dice.getObservationSeq());
    }
}
