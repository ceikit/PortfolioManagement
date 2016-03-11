package Portfolio

import rx.lang.scala.Observable

/**
 * Created by ceikit on 02/01/16.
 */

// from Seq[Obs[Double] ]  to Obs[ Seq[Double] ]
case class ObservableTransform[T]( obs: Seq[Observable[T]] ) {
  
  def transform : Observable[Seq[T]] = {
    obs.foldRight( Observable.just(Seq[T]()).repeat )( (a,b) => a.zipWith(b)( (xs,x) => x :+ xs) )
  }


}
