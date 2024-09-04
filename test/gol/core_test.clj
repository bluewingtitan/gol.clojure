(ns gol.core-test
  (:require [clojure.test :refer :all]
            [gol.core :refer :all]))

(def empty-gamestate '[])

(def zeroed-gamestate '[[0 0 0 0]
                        [0 0 0 0]
                        [0 0 0 0]
                        [0 0 0 0]])

(def blinker-after-1 '[[0 0 0 0 0]
                       [0 0 0 0 0]
                       [0 1 1 1 0]
                       [0 0 0 0 0]
                       [0 0 0 0 0]])

; assumption: glider should crash into corner and form a block
(def glider-final-gamestate '[[0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                              [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                              [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                              [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                              [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                              [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                              [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                              [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                              [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                              [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                              [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                              [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                              [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                              [0 0 0 0 0 0 0 0 0 0 0 0 0 1 1]
                              [0 0 0 0 0 0 0 0 0 0 0 0 0 1 1]])

(deftest test-handle-empty
  (testing "Correctly handle empty gamestate"
    (is (= empty-gamestate (do-evolution empty-gamestate)))))

(deftest test-keep-zeroed
  (testing "A gamestate of only zeroes shall always lead to itself"
    (is (= zeroed-gamestate (do-evolution zeroed-gamestate)))))

(deftest test-stable-gamestate
  (testing "Stable configuration stays stable"
    (is (= test-gamestate (evolute-x-times test-gamestate 10)))))

(deftest test-blinker-gamestate
  (testing "Blinker blinks"
    (is (= blinker-after-1 (do-evolution blinker-gamestate)))))

(deftest test-glider-gamestate
  (testing "Glider terminates in block"
    (is (= glider-final-gamestate (evolute-x-times glider-gamestate 100)))))
