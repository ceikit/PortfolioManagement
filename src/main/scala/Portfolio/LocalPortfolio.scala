package Portfolio

/**
 * Created by ceikit on 27/12/15.
 */

case class LocalStock(ric: String, price: Double)

case class LocalStockPosition(stock: LocalStock, shares: Double)

case class LocalPortfolioPosition(position: Seq[LocalStockPosition]){

  def exposureVolume = position.foldLeft(0.0)( (z, p) => z + p.shares)

  def exposureValue = position.foldLeft(0.0)( (z,v) => z + v.shares * v.stock.price )

}

case class LocalPortfolio(cash: Double, localPortfolioPosition: LocalPortfolioPosition) {

  val numberOfStocks = localPortfolioPosition.position.length

  def portfoliOValue : Double = cash + localPortfolioPosition.exposureValue
}
