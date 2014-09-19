(ns triangulate.core
	(:require [compojure.core :refer :all]
						[compojure.route :as route]
						[compojure.handler :refer [site]]
						[org.httpkit.server :refer :all]
						[ring.middleware.json :refer [wrap-json-params wrap-json-response]]
						[clojure.core.async :as async :refer [chan go <! >!]]
            [triangulate.probes :as probes]
            [triangulate.redis :refer (wcar*)]
            [taoensso.carmine :as car])
  (:gen-class))

(def probes-chan (chan))

(defn hello [req] {:status 200 :headers {"Content-Type" "application/json; charset=utf-8"} :body "Hello there"})

(defn probe 
  "gets a probe request and outputs it to a channel to be triangulated"
  [req]
  (go (>! probes-chan req))
  {:status 204 :headers {}})

(defn get-map 
  "returns the map"
  [req]
  {:status 200 :headers {"Content-Type" "application/json; charset=utf-8"} 
    :body (apply assoc {} (wcar* (car/hgetall "map")))})

(defroutes all-routes
  (GET "/" [] hello)
  (POST "/probe" [] probe)
  (GET "/map" [] get-map)
  (route/files "/") ;; static file url, in `public` folder
  (route/not-found "<p>Page not found.</p>")) ;; all other, return 404

(defn -main [& args]
	(run-server (-> (site #'all-routes) wrap-json-params wrap-json-response) {:port 8080})
	(go (while true (probes/handle (<! probes-chan))))
	(println "running..."))

