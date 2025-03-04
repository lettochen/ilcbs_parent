package cn.itcast.test;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class PieDemo {

	public static void main(String[] args) {
		DefaultPieDataset localDefaultPieDataset = new DefaultPieDataset();
		localDefaultPieDataset.setValue("Section A", new Double(43.200000000000003D));
		localDefaultPieDataset.setValue("Section B", new Double(10.0D));
		localDefaultPieDataset.setValue("Section C", new Double(27.5D));
		localDefaultPieDataset.setValue("Section D", new Double(17.5D));
		localDefaultPieDataset.setValue("Section E", new Double(11.0D));
		localDefaultPieDataset.setValue("Section F", new Double(19.399999999999999D));

		JFreeChart localJFreeChart = ChartFactory.createPieChart("Pie Chart Demo 4", localDefaultPieDataset, true, true,
				false);
		PiePlot localPiePlot = (PiePlot) localJFreeChart.getPlot();
		localPiePlot.setNoDataMessage("No data available");
		localPiePlot.setCircular(false);
		localPiePlot.setLabelGap(0.02D);
		localPiePlot.setExplodePercent("Section D", 0.5D);
		localPiePlot.setLabelLinkStyle(PieLabelLinkStyle.CUBIC_CURVE);

		// 显示图形
		ChartFrame chartFrame = new ChartFrame("jk", localJFreeChart);
		chartFrame.setVisible(true);
		chartFrame.pack();

	}
}
