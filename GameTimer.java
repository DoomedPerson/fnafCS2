����   B I
      java/lang/Object <init> ()V	  	 
   	GameTimer secondsRemaining I  java/util/Timer
  	     timer Ljava/util/Timer;  GameTimer$1
     (LGameTimer;)V      �
     scheduleAtFixedRate (Ljava/util/TimerTask;JJ)V
    !  cancel	 # $ % & ' java/lang/System out Ljava/io/PrintStream; ) Timer stopped.
 + , - . / java/io/PrintStream println (Ljava/lang/String;)V
  1  2 (I)V
  4 5  start Code LineNumberTable LocalVariableTable this LGameTimer; initialSeconds stop StackMapTable getSecondsRemaining ()I main ([Ljava/lang/String;)V args [Ljava/lang/String; 	gameTimer 
SourceFile GameTimer.java NestMembers InnerClasses !                   2  6   F     
*� *� �    7       
   	  8       
 9 :     
 ;    5   6   Q     *� Y� � *� � Y*� 	 � �    7            8        9 :    <   6   V     *� � *� � � "(� *�    7            !  # 8        9 :   =      > ?  6   /     *� �    7       & 8        9 :   	 @ A  6   K     � YZ� 0L+� 3�    7       * 
 +  , 8        B C   
  D :   E    F G      H   
        