(ns triangulate.probes
	(:require [triangulate.redis :refer (wcar*)]
						[taoensso.carmine :as car]
						[triangulate.tri :refer [triangulate]]))

(defn update-and-fetch 
  "updates redis with the probe information and fetches the lastest value"
  [probe]
  (let [key (format "%s:stations" (:mac probe))]
  	(-> (wcar* (car/hset key (:station probe) (:distance probe))
  				 (car/hgetall key))
  			last)))

(defn radiuses 
  "transform value from redis to vector of radiuses"
  [value]
  (->> (apply assoc {} value) 
  		(merge {"0" "0" "1" "0" "2" "0"})
  		vals
  		(map #(Float. %))
  		vec))

(defn submit-to-map 
  "submit point to redis"
  [point mac]
  (wcar* (car/hset "map" mac point)))

(defn handle 
  "handles an incoming probe"
  [{probe :params}]
  (println "handling " {:mac (:mac probe) :station (- (Integer. (:station probe)) 1) :distance (:distance probe)})
  (let [mac (:mac probe)]
	  (-> {:mac (:mac probe) :station (- (Integer. (:station probe)) 1) :distance (:distance probe)} 						; the current probe
	  		update-and-fetch	; update redis with current probe and fetch latest value
	  		radiuses
	  		triangulate
	  		(submit-to-map mac))))