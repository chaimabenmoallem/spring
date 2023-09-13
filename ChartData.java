package com.epix.hawkadmin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


@Document(indexName = "dashboard")
public class ChartData {
   // @Id
    private String id;

    private String[] labels;
    private Dataset[] datasets;

    public ChartData() {
    }

    public ChartData(String id, String[] labels, Dataset[] datasets) {
        this.id = id;
        this.labels = labels;
        this.datasets = datasets;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public Dataset[] getDatasets() {
        return datasets;
    }

    public void setDatasets(Dataset[] datasets) {
        this.datasets = datasets;
    }

    //avoir une classe dans une autre classe, ce sont des classes imbriquées.
    // Le mot-clé staticappliqué à une classe imbriquée signifie que la classe imbriquée est un membre statique de la classe externe.
    // Cela signifie qu'il peut être instancié sans instance de la classe externe,
        public static class Dataset {
            private String label;
            private String[] borderColor;
            private int[] data;
            private String[] backgroundColor;




        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String[] getBorderColor() {
            return borderColor;
        }

        public void setBorderColor(String[] borderColor) {
            this.borderColor = borderColor;
        }

        public int[] getData() {
            return data;
        }

        public void setData(int[] data) {
            this.data = data;
        }

        public String[] getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String[] backgroundColor) {
            this.backgroundColor = backgroundColor;
        }


    }
}
