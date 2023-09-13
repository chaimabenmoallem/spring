package com.epix.hawkadmin.services;

import com.epix.hawkadmin.model.ChartData;
import com.epix.hawkadmin.repository.ChartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChartService {

        @Autowired
        private  ChartRepo chartRepo;

    public ChartData getChartData() {
        return chartRepo.findAll().iterator().next();
    }



    //  private final ChartRepo chartRepo;

  /*  @Autowired
    public ChartService(ChartRepo chartRepo) {
        this.chartRepo = chartRepo;
    }*/

    /*public void saveChartData(ChartData chartData) {
        System.out.println("Received JSON Data: " + chartData);
        chartRepo.save(chartData);
    }*/

  /* public ChartData getChartData(String id) {
        ChartData chartData = chartRepo.findById(id).orElse(null);
        System.out.println(chartData);  // This will print the chartData to the console.
        return chartData;
    }*/

   /* public List<ChartData> getAllChartData() {
        Iterable<ChartData> chartDataIterable = chartRepo.findAll();
        List<ChartData> chartDataList = new ArrayList<>();
        chartDataIterable.forEach(chartDataList::add);
        return chartDataList;
    }*/


  /* public void saveStaticData() {
        // Create an instance of ChartData
        ChartData chartData = new ChartData();

        // Set the unique ID
        String uniqueId = UUID.randomUUID().toString();
        chartData.setId(uniqueId);

        // Set the labels
        chartData.setLabels(new String[]{"chaima", "olfa", "Yosra"});

        // Create instances of Dataset
        ChartData.Dataset dataset1 = new ChartData.Dataset();
        dataset1.setLabel("# of Votes");
        dataset1.setBorderColor(new String[]{"rgba(255, 99, 132, 0.2)", "rgba(255, 159, 64, 0.2)", "rgba(255, 205, 86, 0.2)"});
        dataset1.setData(new int[]{12, 19, 3});
        dataset1.setBackgroundColor(new String[]{"rgba(255, 99, 132, 0.2)", "rgba(255, 159, 64, 0.2)", "rgba(255, 205, 86, 0.2)"});

        ChartData.Dataset dataset2 = new ChartData.Dataset();
        dataset2.setLabel("# of Votes");
        dataset2.setBorderColor(new String[]{"rgba(75, 192, 192, 0.2)", "rgba(54, 162, 235, 0.2)", "rgba(153, 102, 255, 0.2)"});
        dataset2.setData(new int[]{14, 9, 23});
        dataset2.setBackgroundColor(new String[]{"rgba(75, 192, 192, 0.2)", "rgba(54, 162, 235, 0.2)", "rgba(153, 102, 255, 0.2)"});

        // Add the Dataset instances to the datasets array in ChartData
        chartData.setDatasets(new ChartData.Dataset[]{dataset1, dataset2});

        // Save ChartData instance using the repository
        chartRepo.save(chartData);
    }*/










}
