(ns triangulate.redis
	(:require [taoensso.carmine :as car :refer (wcar)]))

(def server1-conn {:pool {} :spec {:uri (or (System/getenv "DATABASE_URL") nil)}})
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))
