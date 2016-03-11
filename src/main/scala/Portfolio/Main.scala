package Portfolio

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.chart.{LineChart, NumberAxis, XYChart}
import javafx.stage.Stage
import scala.concurrent.duration._


import main._
import main.types._
import rx.lang.scala.Observable

import scala.util.Success


/**
 * Created by ceikit on 25/12/15.
 */


object Main {

  def main(args: Array[String]) {

    new PortfolioReactivePlot().launch()

  }




  class PortfolioReactivePlot extends Application {


    override def start(stage: Stage): Unit = {

      stage.setTitle("Portfolio Management")

      //defining the axes
      val xAxis = new NumberAxis()
      val yAxis = new NumberAxis()
      xAxis.setLabel("Ticks")

      //creating the chart
      val lineChart = new LineChart[Number, Number](xAxis, yAxis)

      lineChart.setTitle("Portfolio")
      //defining a series

      val scene = new Scene(lineChart, 1200, 800)


      stage.setScene(scene)
      stage.show()

      val index = Observable.interval(20 milliseconds).onBackpressureBuffer


      val cash = Observable.just(1.0).repeat

      val uno = heston().map( _._2).publish
      val due = heston().map( _._2).publish
      val tre = heston().map( _._2).publish

      val fiat = Stock("Fiat" , uno)
      val ford = Stock("Ford" , due)
      val casa = Stock("Casa" , tre)

      val position = List (StockPosition(fiat, cash), StockPosition(ford, cash), StockPosition(casa, cash) )

      val portfolio = Portfolio( cash, PortfolioPosition( position ))


      readLine()




      val prova = PortfolioPlot(portfolio)


      index.zip(prova.result).observeOn(FXScheduler()).subscribe(v => {

        prova.series.zip(v._2).map( p => p._1.getData.add( new XYChart.Data(v._1, p._2) ) )
        println( v._2, v._1, v._2.reverse.tail.sum )

      } )

      uno.connect
      due.connect
      tre.connect



      prova.series.map( lineChart.getData.add(_) )





      /// THIS IS THE SHIT

      //lineChart.getData.addAll( series)
    }

    def reactiveResults(): Observable[Double] = {
      val t0: Time = 0 // initial time
      val S0: Double = 10 // initial value of the stock
      val N = 1000 // number of time-steps
      val T: Time = 5 // maturity
      val dt = (T - t0) / N // time-step length


      val ceikit = new GeometricBrownian(0.05, 0.5, Success(S0), Discretization.euler)
      //val ceikit = new CIR(10, 0.04, 0.5, Success(S0), Discretization.varianceQuadraticExponential)

      println("start")

      S0 +: ReactiveEvolution.reactiveRun(ceikit, t0, T, dt)

    }



    def heston(): Observable[(Double,Double)] = {

      val t0: Time = 0 // initial time
      val N = 1000 // number of time-steps
      val T: Time = 1 // maturity
      val dt = (T - t0) / N // time-step length
      val S0: Double = 10 // initial value of the stock
      val v0: (Double, Double) = (0.044 ,0.044)
      val ceikit = new Heston(0.03, - 0.6288, 14.829, 0.04, 0.7, v0, Success(S0), Discretization.stockQE)

      (v0._2 , S0) +: EvolutionHeston.runHeston(ceikit, t0, T, dt)

    }

    // (k 1.4829, thetha 0.0872, nu 1.7048, rho −0.6288, v 0.0104)
    // 0.03, -0.7, 20, 0.04, 0.9



    def launch() {
      Application.launch( )
    }
  }


}