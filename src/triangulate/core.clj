(ns triangulate.core
	(:require [compojure.core :refer :all]
						[compojure.route :as route]
						[compojure.handler :refer [site]]
						[org.httpkit.server :refer :all]
						[clojure.core.async :as async :refer :all])
  (:gen-class))

(def probes (chan))

(defn hello [req] {:status 200 :headers {"Content-Type" "application/json; charset=utf-8"} :body "Hello there"})

(defn probe 
  "gets a probe request and outputs it to a channel to be triangulated"
  [req]
  (go (>! probes req))
  {:status 200 :headers {"Content-Type" "application/json; charset=utf-8"} :body "got probe"})

(defroutes all-routes
  (GET "/" [] hello)
  (POST "/probe" [] probe)
  (route/files "/") ;; static file url, in `public` folder
  (route/not-found "<p>Page not found.</p>")) ;; all other, return 404

(defn -main [& args]
	(run-server (site #'all-routes) {:port 8080})
	(go (while true (println (<! probes))))
	(println "running..."))

