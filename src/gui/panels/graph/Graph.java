package gui.panels.graph;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Map;

public class Graph {

    private JFreeChart chart;
    private ChartPanel chartPanel;
    private JPanel mainPanel;
    private Map<String, XYSeries> seriesMap = new HashMap<>();

    private double minY = Double.MAX_VALUE;
    private double maxY = Double.MIN_VALUE;
    private double minX = Double.MAX_VALUE;
    private double maxX = Double.MIN_VALUE;

    public Graph(String title, String xLabel, String yLabel) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        this.chart = ChartFactory.createXYLineChart(
                title,
                xLabel,
                yLabel,
                dataset
        );
        this.chart.setPadding(new RectangleInsets(10, 10, 10, 30));

        XYPlot plot = this.chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setDefaultShapesVisible(false);
        renderer.setDefaultLinesVisible(true);
        plot.setRenderer(renderer);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setNumberFormatOverride(new NumberFormat() {
            @Override
            public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
                int totalSeconds = (int) Math.round(number);
                int h = (totalSeconds / 3600);
                int m = (totalSeconds % 3600) / 60;
                int s = totalSeconds % 60;
                return toAppendTo.append(String.format("%02d:%02d:%02d", h, m, s));
            }

            @Override
            public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
                return format((double) number, toAppendTo, pos);
            }

            @Override
            public Number parse(String source, ParsePosition parsePosition) {
                return null;
            }
        });

        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        this.chart.setAntiAlias(true);
        rangeAxis.setAutoRangeIncludesZero(false);
        domainAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());

        this.chartPanel = new ChartPanel(this.chart);
        this.chartPanel.setPreferredSize(new Dimension(1200, 400));
        this.chartPanel.setMouseWheelEnabled(true);

        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.add(this.chartPanel, BorderLayout.CENTER);
    }

    public JPanel getChartPanel() {
        return this.mainPanel;
    }

    public void addPoint(double capacity, double averageTime, String seriesName) {
        XYSeries series = this.seriesMap.get(seriesName);

        if (series == null) {
            series = new XYSeries(seriesName);
            this.seriesMap.put(seriesName, series);
            XYPlot plot = this.chart.getXYPlot();
            ((XYSeriesCollection) plot.getDataset()).addSeries(series);
        }

        series.add(capacity, averageTime);

        this.minY = Math.min(this.minY, averageTime);
        this.maxY = Math.max(this.maxY, averageTime);
        this.minX = Math.min(this.minX, capacity);
        this.maxX = Math.max(this.maxX, capacity);

        double marginY = (this.minY == this.maxY) ? 1.0 : 0.0;
        double marginX = (this.minX == this.maxX) ? 1.0 : 0.0;

        XYPlot plot = this.chart.getXYPlot();
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        NumberAxis domain = (NumberAxis) plot.getDomainAxis();

        range.setRange(this.minY - marginY, this.maxY + marginY);
        domain.setRange(this.minX - marginX, this.maxX + marginX);

        this.refresh();
    }

    public void refresh() {
        this.chartPanel.revalidate();
        this.chartPanel.repaint();
    }

    public void reset() {
        XYPlot plot = this.chart.getXYPlot();
        XYSeriesCollection dataset = (XYSeriesCollection) plot.getDataset();

        dataset.removeAllSeries();
        this.seriesMap.clear();
        this.minY = Double.MAX_VALUE;
        this.maxY = Double.MIN_VALUE;
        this.minX = Double.MAX_VALUE;
        this.maxX = Double.MIN_VALUE;

        this.refresh();
    }
}