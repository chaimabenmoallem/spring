package com.epix.hawkadmin.controller;

import com.epix.hawkadmin.model.ChartData;
import com.epix.hawkadmin.repository.ChartRepo;
import com.epix.hawkadmin.services.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping ("/dashboard")
@RestController
public class ChartController {

    private final ChartService chartService;
    private final ChartRepo chartRepo;


    @Autowired
    public ChartController(ChartService chartService , ChartRepo chartRepo) {
        this.chartService = chartService;
        this.chartRepo = chartRepo;
    }


   /* @GetMapping
    public ChartData getChartData() {
        return chartService.getChartData();
    }*/



    @GetMapping
    public ResponseEntity<ChartData> getChartData() {
        return ResponseEntity.ok(chartRepo.findAll().iterator().next());
    }


  /* @RequestMapping
    public ResponseEntity<Void> saveStaticData() {
        chartService.saveStaticData();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChartData> getChartData(@PathVariable String id) {
        ChartData chartData = chartService.getChartData(id);
        if(chartData != null)
            return new ResponseEntity<>(chartData, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }*/




   /* @PostMapping("/saveDoughnutChartData")
    public ResponseEntity<Void> saveDoughnutChartData() {
        chartService.saveDoughnutChartData();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/saveLineChartData")
    public ResponseEntity<Void> saveLineChartData() {
        chartService.saveLineChartData();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
*/

   /* @GetMapping("/dashboard/all")
    public ResponseEntity<List<ChartData>> getAllChartData() {
        List<ChartData> chartDataList = chartService.getAllChartData();
        if(chartDataList != null && !chartDataList.isEmpty())
            return new ResponseEntity<>(chartDataList, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }*/



  /*  @PostMapping("/")
    public ResponseEntity<String> saveChartData(@RequestBody ChartData chartData) {
        chartService.saveChartData(chartData);
        return ResponseEntity.ok("Data saved successfully");
    }*/


}
