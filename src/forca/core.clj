(ns forca.core
  (:gen-class))

(def total-de-vidas 6)
(def palavra-secreta "MELANCIA")

(defn perdeu [] (print "Você perdeu"))

(defn ganhou [] (print "Você ganhou"))


(defn letras-faltantes [palavra acertos]
	(remove (fn [letra] (contains? acertos (str letra))) palavra))

(defn acertou-a-palavra-toda? [palavra acertos] 
	(empty? (letras-faltantes palavra acertos)))

; o trecho ".contains" está invocando o método contains da classe String, ou seja, "palavra.contains(chute)".
; O "." antes do método indica que se pode utilizar métodos que se encontram na JVM.
(defn acertou? [chute palavra]
	(.contains palavra chute))

; A exclamação indica que "a função muda o estado", ou seja, faz leitura do disco, leitura do teclado, salva no Banco de Dados, etc 
(defn le-letra! [] (read-line))

; doseq é uma função estilo a Map (de loop) no entanto não é uma função Preguiçosa, ou seja, é executado no ato.

(defn imprime-forca [vidas palavra acertos]
	(println "Vidas " vidas)
	(doseq [letra (seq palavra)] 
		(if (contains? acertos (str letra)) 
			(print letra " ") 
			(print "_ ")))
	(println))

(defn jogo [vidas palavra acertos]
	(imprime-forca vidas palavra acertos)
	(cond
		(= vidas 0) (perdeu)
		(acertou-a-palavra-toda? palavra acertos) (ganhou)
		:else
		(let [chute (le-letra!)]
			(if (acertou? chute palavra)
				(do
					(println "Acertou a letra")	
					(recur vidas palavra (conj acertos chute)))
				(do
					(println "Errou a letra! Vidas restantes: " (dec vidas))
					(recur (dec vidas) palavra acertos))))))

; Recursão de cauda: Quando a última linha da função faz uma chamada para ela mesma, o compilador sabe que as funções NÃO devem ser empilhadas na stack.

(defn soma [n]
	(loop [contador 1 soma 0]
		(if (> contador n) soma
		(recur (inc contador) (+ soma contador)))))

; Exemplo de utilização do reduce
; Map aplica a função desejada sobre os elementos da lista e retorna uma nova Lista.
; Reduce aplica a função desejada sobre os elemtnos da lista e retorna um único Valor.
; '->>' é um atalho que permite com que eu escreva as funções efetuadas em cima da lista na forma abaixo, 
; ou seja, mantendo a ordem de execução das funções numa maneira mais compreensível para a leitura.

(defn testa-reduce []
	(def carros [50000 60000]) 
	(print 
		(->> carros
			(map (fn [x] (* x 2))) ; Iteramos a lista carros e multiplicamos cada elementod a lista por 2
			(map (fn [x] (* x 3))) ; Iteramos a lista carros e multiplicamos cada elementod a lista por 3
			(reduce (fn [acc n] (+ acc n)))))) ; Iteramos a lista carros e consolidamos todos os valores da lista


(defn comeca-o-jogo [] (jogo total-de-vidas palavra-secreta #{}))

; lein run - Executa o método Main
; lein uberjar - Gera um executável da aplicãção Clojure, o arquivo *.jar.
(defn -main [& args]
  (comeca-o-jogo))