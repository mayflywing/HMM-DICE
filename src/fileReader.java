import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class fileReader {

    public static  void readFileByLines(String fileName,HMM_DICE hmm_dice) {
        File file = new File(fileName);
        BufferedReader reader = null;
        ArrayList<String> lines = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                if(tempString.charAt(0)!='#') {
                    lines.add(tempString);
                }
            }
            reader.close();

            //# Probability of not switching
            double p = Double.valueOf(lines.get(0));
            double tp = (1-p)/2;
            double[][] transitionProb ={
                    {p,tp,tp},
                    {tp,p,tp},
                    {tp,tp,p}
            };
            hmm_dice.setTransitionProb(transitionProb);

            //# Emission probabilities
            double[][] emissionProb = new double[3][3];
            String[] tempEmission;
            for (int i = 1; i < 4; i++) {
                tempEmission = lines.get(i).split(",");
                for (int j = 0; j < tempEmission.length; j++) {
                    emissionProb[i-1][j] = Double.valueOf(tempEmission[j]);
                }
            }
            hmm_dice.setEmissionProb(emissionProb);

            //# Emissions
            String[] tempObservation;
            tempObservation = lines.get(4).substring(1,lines.get(4).length()-1).split(",");
            int[] ObservationSeq = new int[tempObservation.length];
            for (int i = 0; i < tempObservation.length; i++){
                ObservationSeq[i] = Integer.valueOf(tempObservation[i].trim());
            }
            hmm_dice.setObservationSeq(ObservationSeq);




        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}
