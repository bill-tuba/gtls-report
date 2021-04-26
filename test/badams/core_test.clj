(ns badams.core-test
  (:require [badams.core :as sut]
            [clojure.test :refer :all]
            [badams.core :as core]))

(def date "12/12/1979")

(def details {:LastName      "LN"
              :FirstName     "FN"
              :Email         "EM"
              :FavoriteColor "FC"
              :DateOfBirth   (sut/date date)})

(def csv-line "LN,FN,EM,FC,12/12/1979")
(def spv-line "LN FN EM FC 12/12/1979")
(def psv-line "LN|FN|EM|FC|12/12/1979")

(def expected-details {:LastName      "LN"
                       :FirstName     "FN"
                       :Email         "EM"
                       :FavoriteColor "FC"
                       :DateOfBirth   date})

(deftest parse-test
  (and
   (is (= nil (sut/parse nil)))
   (is (= nil (sut/parse "")))

   (doseq [line [csv-line spv-line psv-line]]
     (is (= details (sut/parse line))))))

(deftest prepare-details-test
  (is (= expected-details (sut/prepare details))))