(defproject triangulate "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.4.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.338.0-5c5012-alpha"]
                 [http-kit "2.1.16"]
                 [ring/ring-json "0.3.1"]
                 [compojure "1.1.9"]
                 [org.clojure/math.combinatorics "0.0.8"]
                 [com.taoensso/carmine "2.6.2"]
                 [javax.servlet/servlet-api "2.5"]]
  :main ^:skip-aot triangulate.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [org.clojure/tools.namespace "0.2.6"]]}})
