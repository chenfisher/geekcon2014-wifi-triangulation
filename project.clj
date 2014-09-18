(defproject triangulate "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.338.0-5c5012-alpha"]
                 [http-kit "2.1.16"]
                 [compojure "1.1.9"]]
  :main ^:skip-aot triangulate.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[javax.servlet/servlet-api "2.5"]]}})
