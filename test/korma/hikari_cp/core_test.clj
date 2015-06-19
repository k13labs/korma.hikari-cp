(ns korma.hikari-cp.core-test
  (:require [clojure.test :refer :all]
            [korma.hikari-cp.core :refer :all]
            [korma.db :refer :all :exclude [make-pool]]))

(def db-config-with-defaults
  {:classname "org.h2.Driver"
   :subprotocol "h2"
   :subname "mem:db_connectivity_test_db"
   :user "bob"
   :password "password"})

(def db-config-with-options-set
  {:classname "org.h2.Driver"
   :subprotocol "h2"
   :subname "mem:db_connectivity_test_db"
   :idle-timeout 88
   :maximum-pool-size 20
   :test-connection-query "SELECT 1"
   :auto-commit false
   :connection-timeout 123000
   :max-lifetime 234000
   :minimum-idle 25
   :useUnicode true})

(def db-config-with-jdbc-url-set
  {:jdbc-url "jdbc:h2:mem:db_jdbc_url_test_db"
   :classname "org.h2.Driver"})

(def db-config-with-ds-classname-set
  {:ds-classname "org.h2.jdbcx.JdbcDataSource"
   :datasource.database "mem:ds_test_db"})

(deftest connection-pooling-default-test
  (let [pool (make-pool db-config-with-defaults)
        datasource (:datasource pool)]
    (is (= "org.h2.Driver" (.getDriverClassName datasource)))
    (is (= "jdbc:h2:mem:db_connectivity_test_db" (.getJdbcUrl datasource)))
    (is (= "bob" (.getUsername datasource)))
    (is (= "password" (.getPassword datasource)))
    (is (= 600000 (.getIdleTimeout datasource)))
    (is (= 15 (.getMaximumPoolSize datasource)))
    (is (= true (.isAutoCommit datasource)))
    (is (= 30000 (.getConnectionTimeout datasource)))
    (is (= 1800000 (.getMaxLifetime datasource)))
    (is (= 15 (.getMinimumIdle datasource)))
    (is (= nil (.getConnectionTestQuery datasource)))))

(deftest connection-pooling-test
  (let [pool (make-pool db-config-with-options-set)
        datasource (:datasource pool)]
    (is (= 88 (.getIdleTimeout datasource)))
    (is (= 20 (.getMaximumPoolSize datasource)))
    (is (= {"useUnicode" "true"}
           (.getDataSourceProperties datasource)))
    (is (= false (.isAutoCommit datasource)))
    (is (= 123000 (.getConnectionTimeout datasource)))
    (is (= 234000 (.getMaxLifetime datasource)))
    (is (= 25 (.getMinimumIdle datasource)))
    (is (= "SELECT 1" (.getConnectionTestQuery datasource)))))

(deftest connection-pooling-ds-classname-test
  (let [pool (make-pool db-config-with-ds-classname-set)
        datasource (:datasource pool)]
    (is (= {"datasource.database" "mem:ds_test_db"}
           (.getDataSourceProperties datasource)))
    (is (= "org.h2.jdbcx.JdbcDataSource" (.getDataSourceClassName datasource)))))

(deftest connection-pooling-jdbc-url-test
  (let [pool (make-pool db-config-with-jdbc-url-set)
        datasource (:datasource pool)]
    (is (= "jdbc:h2:mem:db_jdbc_url_test_db" (.getJdbcUrl datasource)))
    (is (= "org.h2.Driver" (.getDriverClassName datasource)))))

(def testdb1-spec
  (h2 {:db "mem:testdb1"
       :delimiters ["`" "`"]
       :alias-delimiter " az "
       :naming {:keys clojure.string/upper-case
                :fields clojure.string/upper-case}}))

(deftest can-defdb-with-connection-pool
  (let [options [:subprotocol :delimiters :alias-delimiter :naming]
        pool (make-pool testdb1-spec)]
    (defdb testdb1 pool)
    (is (= pool (:pool testdb1)))
    (is (= (select-keys testdb1-spec options) (select-keys (:options testdb1) options)))))
