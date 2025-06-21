package app.main;

//import app.io.CSVReader;
//import app.model.Dup;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartUtils;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.axis.CategoryAxis;
//import org.jfree.chart.axis.CategoryLabelPositions;
//import org.jfree.chart.plot.CategoryPlot;
//import org.jfree.chart.ui.RectangleInsets;
//import org.jfree.data.category.DefaultCategoryDataset;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//import app.io.CSVReader;
//import app.model.Dup;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartUtils;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.axis.CategoryAxis;
//import org.jfree.chart.axis.CategoryLabelPositions;
//import org.jfree.chart.plot.CategoryPlot;
//import org.jfree.chart.ui.RectangleInsets;
//import org.jfree.data.category.DefaultCategoryDataset;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
public class Diagram {
//    static final String CURRENT_DIR = System.getProperty("user.dir");
//    static final String FILE_1 = CURRENT_DIR + "/data/Z2.csv";
//    static final String FILE_2 = CURRENT_DIR + "/data/ZY2.csv";
//
//    public static void main(String[] args) {
//        CSVReader dr = new CSVReader(new File(FILE_2).getAbsolutePath());
//        ArrayList<Dup> dupes = dr.read(Dup.class);
//
//        HashMap<Integer, Integer> idCount = new HashMap<>();
//
//        for (Dup d : dupes) {
//            int lid_count = 1 + idCount.getOrDefault(d.getLid(), 0);
//            int rid_count = 1 + idCount.getOrDefault(d.getRid(), 0);
//            idCount.put(d.getLid(),  lid_count);
//            idCount.put(d.getRid(),  rid_count);
//        }
//
//        Map<Integer, Long> groupedIdCount = idCount.values().stream()
//                .collect(Collectors.groupingBy(
//                        Function.identity(),       // Gruppierungskriterium: der Value selbst
//                        Collectors.counting()      // Zählung pro Value
//                ));
//
//        groupedIdCount.forEach((key, val) ->
//                System.out.println(key + " kommt " + val + "x vor"));
//
//        System.out.println(idCount.values().stream().mapToLong(v -> v).sum()); // passt. = 2x Anz. Zeilen
//        System.out.println(groupedIdCount.entrySet().stream().mapToLong(e -> e.getKey() * e.getValue()).sum());
//
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        groupedIdCount.entrySet()
//                .stream()
//                .sorted(Map.Entry.comparingByKey())
//                .forEach(entry -> {
//                    dataset.addValue(entry.getValue() * entry.getKey(), "y", entry.getKey());
//                });
//
//        JFreeChart chart = ChartFactory.createBarChart(
//                "grouped dup counts", // Titel
//                "x",                  // X-Achse
//                "y",                // Y-Achse
//                dataset
//        );
//
//        CategoryPlot plot = (CategoryPlot) chart.getPlot();
//        plot.setInsets(new RectangleInsets(50, 50, 50, 50));
//
//        CategoryAxis domainAxis = plot.getDomainAxis();
//        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
//
//        File outputFile = new File("balkendiagramm.png");
//        try {
//            ChartUtils.saveChartAsPNG(outputFile, chart, 2000, 1600);
//            System.out.println("Diagramm gespeichert unter: " + outputFile.getAbsolutePath());
//        } catch (
//                IOException e) {
//            e.printStackTrace();
//        }
//    }
}
