package Portfolio

import rx.lang.scala.Observable

/**
 * Created by ceikit on 23/12/15.
 */


case class Stock(ric: String, price: Observable[Double])

case class StockPosition(stock: Stock, shares: Observable[Double])

case class PortfolioPosition (position: Seq[StockPosition]) {

  val util: Observable[Double] = Observable.just( 0.0 ).repeat

  def exposureVolume : Observable[Double] = {
    position
      .map(_.shares)
      .fold(util)((a, b) => a.zipWith(b)(_ + _))
  }

  def exposureValue : Observable[Double] = {

    position
      .map(p => p.shares.zipWith(p.stock.price)(_ * _))
      .fold(util)((a, b) => a.zipWith(b)(_ + _))
  }
}


case class Portfolio (cash: Observable[Double], portfolioPosition: PortfolioPosition) {

  val numberOfStocks = portfolioPosition.position.length

  def portfolioValue : Observable[Double] =
    cash.zipWith(portfolioPosition.exposureValue)( _ + _ )
}
