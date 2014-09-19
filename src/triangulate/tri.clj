(ns triangulate.tri
	(:require [clojure.math.combinatorics :as comb]))

(defrecord Point [x y])

; 1 32.488766, 34.890636
; 2 32.488921, 34.889385
; 3 32.488160, 34.890368

(def stations [(Point. 270 340)
							 (Point. 570 440)
							 (Point. 410 540)])

(defrecord Triangular [a b c])

(defn angle 
	[a b]
	(let [dx (Math/abs (- (:x a) (:x b)))
				dy (Math/abs (- (:y a) (:y b)))]
		(Math/atan (/ dx dy))))

(defn sign 
	[a b]
	(if (> a b)
		-1
		1))

(defn intersection
	[a b]
		(let [station (first a)
					angle (angle (first a) (first b))
					radius (second a)
					sign-x (sign (:x (first a)) (:x (first b)))
					sign-y (sign (:y (first a)) (:y (first b)))
					x (-> angle
								Math/sin
								(* radius)
								(* sign-x)
								(+ (:x station)))
					y (-> angle
								Math/cos
								(* radius)
								(* sign-y)
								(+ (:y station)))]
			(Point. x y)))

(defn intersections
	[a b]
	[(intersection a b) (intersection b a)])

(defn average-point
	[points]
	(let [c (count points)
				ps (map (juxt :x :y) points)
				xs (reduce + (map first ps))
				ys (reduce + (map second ps))]
		[(/ xs c) (/ ys c)]))


(defn triangulate 
	[radiuses]
	(-> (map #(apply intersections %) (comb/combinations (partition 2 (interleave stations radiuses)) 2))
			flatten
			average-point))



