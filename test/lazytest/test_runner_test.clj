(ns lazytest.test-runner-test
  (:require [clojure.test :refer [deftest testing is]]
            [lazytest.core :refer [defdescribe expect it]]))

(defdescribe lazy-test "This is a lazytest test"
  (it "should run when by default"
    (expect (= 1 1))))

(deftest clojure-test
  (testing "should only run with 'plus' runner"
    (is (= 1 1))))
