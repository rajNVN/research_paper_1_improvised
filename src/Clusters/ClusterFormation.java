package Clusters;

//2.2secs;

import javax.sound.midi.SysexMessage;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import static Algorithm3.Haversine.calculateHaversine;
import static Clusters.getMaxMin.getTypeId;
import static UserInput.input.*;


public class ClusterFormation {
    public static void main(String args[]) throws SQLException, FileNotFoundException {
        for(File ip:new File("G:\\euro").listFiles()) {
            if (ip.getName().contains("quadtree")||ip.getName().contains("base"))
                continue;
            long t11 = System.nanoTime();
            HashMap<Double, String> mq = Clusters.getMaxMin.getSetList();
            List<Double> set1 = Clusters.getMaxMin.getTotalTerms();

            Double max = set1.get(set1.size() - 1);
            Double min = set1.get(0);
            Double start = min;
            Double Diff = max - min;
            Double addUp = Diff / 10;

            MainClusters clusters[] = new MainClusters[10];
            int i;
            for (i = 0; i < 10; i++) {
                clusters[i] = new MainClusters(start, start + addUp, i);
                start = start + addUp;
            }

            for (i = 0; i < 10; i++) {
                Double cmin = clusters[i].getRangeStart();
                Double cmax = clusters[i].getRangeEnd();
                List<Double> inputList = Clusters.getMaxMin.getInputList(cmin, cmax);
                int countobj = 0;
                int j = 0;
                while (j < inputList.size()) {
                    String name = mq.get(inputList.get(j));
                    Double weight = inputList.get(j);
                    String type = Clusters.getMaxMin.SendType(inputList.get(j));
                    String LonLat = Clusters.getMaxMin.SendLongLat(inputList.get(j));
                    String s[] = LonLat.split("##");
                    Double Lo = Double.parseDouble(s[0]);
                    Double La = Double.parseDouble(s[1]);
                    int id = clusters[i].indexOfCluster(type);
                    clusters[i].addObjsToClusters(id, countobj, name, type, weight, Lo, La);
                    j++;
                    countobj++;
                }
                clusters[i].addObjsEnd();
            }

            for (i = 0; i < 10; i++) {
                if (i > 0) {
                    for (int h = 0; h < Clusters.getMaxMin.getType().size(); h++) {
                        clusters[i - 1].subclusters[h].right = clusters[i].subclusters[h];
                        clusters[i].subclusters[h].left = clusters[i - 1].subclusters[h];
                    }

                }
            }
            long t12 = System.nanoTime();

            String in[] = takeInput(ip);

            HashMap<String, List<String>> typelist = getTypeInputs();
            HashMap<Integer, Double> userLocation = getLoc();
            HashMap<String, Integer> typeId = getTypeId();

            System.setOut(new PrintStream("G:\\euro\\imp("+ip.getName().split("\\.")[0]+").txt"));
            for (int t = 0; t < in.length; t++) {
                int clusterid = 0;
                Double uR = userLocation.get(t);
                String ul[] = getLocation1(t).split(",");
                Double ulon = Double.parseDouble(ul[1]);
                Double ulat = Double.parseDouble(ul[0]);
                System.out.println("Result For user :" + (t + 1));
                for (i = 0; i < clusters.length; i++) {
                    Double maxR = clusters[i].getRangeEnd();
                    Double minR = clusters[i].getRangeStart();
                    //  System.out.println(maxR + "----" + minR);
                    // System.out.println(uR);
                    if (uR <= maxR && uR >= minR) {
                        clusterid = i;
                        // System.out.println("Getting in here");
                        break;
                    }
                }


                List<Objects> userResult = new ArrayList<Objects>();
                List<String> rt = typelist.get(in[t]);
                for (int k = 0; k < rt.size(); k++) {

                    int id = typeId.get(rt.get(k));
                    id--;
                    TypeCluster temp = clusters[clusterid].getSubclusters(id);
                    userResult = temp.displayAllObjsName(in[t], userResult);
                    userResult = clusters[clusterid].traverseSubclusters(id, in[t], userResult);
                }
                int top_k = Integer.parseInt(ip.getName().split("_")[2].split("\\.")[0]);
                if (userResult.size() > 0) {
                    HashMap<Double, Objects> m1 = new HashMap<>();
                    List<Double> lsd = new ArrayList<>();
                    for (Objects h : userResult) {
                        Double rw = calculateHaversine(ulon, ulat, h.getLong(), h.getLat());
                        m1.put(rw, h);
                        lsd.add(rw);
                    }
                    Collections.sort(lsd);
                    int count = 0;
                    for (Double fg : lsd) {
                        if (count >= top_k)
                            break;
                        Objects sd = m1.get(fg);
                        System.out.println(fg + "-->" + sd.getLat() + "-->" + sd.getLong() + "-->" + sd.getName() + "-->" + sd.getType());
                        count++;
                    }
                }

            }

            long t2 = System.nanoTime();
            double ctime = (double) (t12 - t11) / (Math.pow(10, 9));
            double etime = (double) (t2 - getT1()) / (Math.pow(10, 9));
            System.out.println("Time Required for Cluster Formation :" + ctime);
            System.out.println("Time Required to provide the user result :" + etime);
            System.out.println("Total Time:" + (ctime + etime));
        }
    }
}
