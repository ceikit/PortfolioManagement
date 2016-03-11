package Portfolio

import javafx.scene.chart.XYChart.Series
import javafx.scene.chart.{NumberAxis, XYChart}

import rx.lang.scala.Observable

/**
 * Created by ceikit on 25/12/15.
 */

case class PortfolioPlot(portfolio: Portfolio) {

  val xAxis = new NumberAxis()
  val yAxis = new NumberAxis()
  xAxis.setLabel("Ticks")

  val series: Seq[Series[Number, Number]] =
    Array.fill( portfolio.numberOfStocks + 2)( new XYChart.Series[Number, Number]() )

  val listToPlot: Seq[Observable[Double]] = portfolio.portfolioPosition.position.map(_.stock.price)

  val result: Observable[Seq[Double]] = ObservableTransform[Double](  portfolio.portfolioValue +: listToPlot ).transform

}
