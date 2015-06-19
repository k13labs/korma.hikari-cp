(defproject korma.hikari-cp "0.1.0"
  :description "HikariCP support for SQL Korma"
  :url "https://github.com/k13labs/korma.hikari-cp"
  :scm {:name "git"
        :url "https://github.com/k13labs/korma.hikari-cp.git"}
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.zaxxer/HikariCP-java6 "2.3.8"]]
  :profiles {:test {:dependencies [[com.h2database/h2 "1.4.187"]
                                   [korma "0.4.2"]]}})
