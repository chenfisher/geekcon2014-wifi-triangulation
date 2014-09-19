(ns triangulate.core
	(:require [compojure.core :refer :all]
						[compojure.route :as route]
						[compojure.handler :refer [site]]
						[org.httpkit.server :refer :all]
						[ring.middleware.json :refer [wrap-json-params]]
						[clojure.core.async :as async :refer [chan go <! >!]]
            [triangulate.probes :as probes])
  (:gen-class))

(def probes-chan (chan))

(defn hello [req] {:status 200 :headers {"Content-Type" "application/json; charset=utf-8"} :body "Hello there"})

(defn probe 
  "gets a probe request and outputs it to a channel to be triangulated"
  [req]
  (go (>! probes-chan req))
  {:status 200 :headers {"Content-Type" "application/json; charset=utf-8"} :body "got probe"})

(defroutes all-routes
  (GET "/" [] hello)
  (ANY "/probe" [] probe)
  (route/files "/") ;; static file url, in `public` folder
  (route/not-found "<p>Page not found.</p>")) ;; all other, return 404

(defn -main [& args]
	(run-server (-> (site #'all-routes) wrap-json-params) {:port 8080})
	(go (while true (probes/handle (<! probes-chan))))
	(println "running..."))

