package Portfolio

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.chart.{XYChart, LineChart, NumberAxis}
import javafx.stage.Stage

import main._
import main.types._
import rx.lang.scala.Observable

import scala.util.Success

/**
 * Created by ceikit on 02/01/16.
 */



object LocalPortfolioMain {

  def main(args: Array[String]) {


    val method  = new LocalPortfolioReactivePlot
    val cash = Observable.from( Range(1,2).map(_.toDouble)).repeat
    val fiat = Stock("Fiat" , method.heston().map( _._2))

    val transform = ObservableTransform( Seq(cash,fiat.price)).transform

    transform.subscribe(v => println(v))


  }
}

class LocalPortfolioReactivePlot extends Application {


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

    val series = new XYChart.Series[Number, Number]()


    val cash = Observable.from( Range(1,2).map(_.toDouble)).repeat
    val fiat = Stock("Fiat" , heston().map( _._2))
    val ford = Stock("Ford" , heston().map( _._2))
    val casa = Stock("Casa" , heston().map( _._2))



    readLine()




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
    val T: Time = 5 // maturity
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
