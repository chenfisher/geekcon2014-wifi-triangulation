(ns triangulate.core
	(:require [compojure.core :refer :all]
						[compojure.route :as route]
						[compojure.handler :refer [site]]
						[org.httpkit.server :refer :all])
  (:gen-class))

(defn hello [req] {:status 200 :headers {"Content-Type" "application/json; charset=utf-8"} :body "Hello there"})

(defroutes all-routes
  (GET "/" [] hello)
  (route/files "/") ;; static file url, in `public` folder
  (route/not-found "<p>Page not found.</p>")) ;; all other, return 404

(defn -main [& args]
	(run-server (site #'all-routes) {:port 8080}))
