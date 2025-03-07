(ns lazytest.test-runner
  (:refer-clojure :exclude [test])
  (:require [clojure.set :as set]
            [cognitect.test-runner :as runner]
            [cognitect.test-runner.api :as api]
            [cognitect.test-runner.protocols :as p]
            [lazytest.find :refer [find-var-test-value]]
            [lazytest.repl :as lt]))

(defn- ns-contains-tests?
  "Check if a namespace contains some tests to be executed."
  [ns]
  (some find-var-test-value (-> ns ns-publics vals)))

(defrecord LazyTestRunner []
  p/TestRunner
  (enable-filtering! [_ _ _] nil)
  (contains-tests? [_ _ ns] (ns-contains-tests? ns))
  (run-tests [this options nses]
             (merge-with
              +
              {:fail 0, :error 0}
              (when-let [nses-with-tests (seq (filter #(p/contains-tests? this options %) nses))]
                (let [lazy-opts {:var :var-filter :namespace :ns-filter}
                      outputs   (seq (filter (complement #{'junit 'tap}) (:output options)))]
                  (lt/run-tests nses-with-tests
                                (-> options
                                    (set/rename-keys lazy-opts)
                                    (dissoc :output)
                                    (cond-> outputs
                                      (assoc :output outputs))))))))
  (disable-filtering! [_ _ _] nil))

(defn create-lazytest-runner []
  (->LazyTestRunner))

(defrecord TwoTestRunners [one two]
  p/TestRunner
  (enable-filtering! [_ options nses]
    (p/enable-filtering! one options nses)
    (p/enable-filtering! two options nses))
  (contains-tests? [_ options ns]
    ;; should never be called directly but this is a reasonable implementation:
    (or (p/contains-tests? one options ns)
        (p/contains-tests? two options ns)))
  (run-tests [_ options nses]
    (merge-with
     +
     {:fail 0, :error 0}
     (p/run-tests one options nses)
     (p/run-tests two options nses)))
  (disable-filtering! [_ options nses]
    (p/disable-filtering! two options nses)
    (p/disable-filtering! one options nses)))

(defn create-lazytest+-runner []
  (->TwoTestRunners (create-lazytest-runner) (runner/create-clojure-test-runner)))

(defn test [options]
  (api/test (assoc options :test-runner-fn 'lazytest.test-runner/create-lazytest-runner)))

(defn test+ [options]
  (api/test (assoc options :test-runner-fn 'lazytest.test-runner/create-lazytest+-runner)))

(defn -main [& args]
  (apply runner/-main "--test-runner-fn" "lazytest.test-runner/create-lazytest-runner" args))
