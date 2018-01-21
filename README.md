# CW-Trainer
Android application for mastering CW skill. (for practices receiving morse code)

### 1. How I started that (Jan 18, 2018).
10 days ago I knew nothing about Android development and Java either. During those 10 days I completed couple courses about Java and Android which actually only scratch the surface. However, I decided that learn by doing a real project much better and efficient. I do not have much time to do this but I am going to spend my time on it after work and during weekends. During work days I am going to collect information and think over realization of it and during weekends try to implement that. 

Do you belive that Morse is faster than Text Messaging? No, check it out - [link](http://c2.com/morse/wiki.cgi?MorseFasterThanTextMessaging).

What do I want to implement? Ok, I am going to organize that as a TODO list for myself. 
## TODO (in priority order)
- [ ] Koch's methods - [link](http://www.justlearnmorsecode.com/koch.html)
- [ ] A Fully Automatic Morse Code Teaching Machine - [link](http://c2.com/morse/)
- [ ] Kind of Training machine to copy HAM calls. Idea from [RufzXP](http://www.rufzxp.net/).
- [ ] Add a feature "the Pileup" into the last item.

It might look like I am going to reinvent wheels, but as I said before, this is just for practice and built up my skills.

### 2. Tone generation. (CwPlayer class)
Ok, first what should this class take as parameters on constractor. 
- [x] Speed - as I know "PARIS" is frequently mentioned as the "standard word" for measuring translation speed. I also know that Speed (WPM) = 2.4 * (Dots per second). In code I am going use WPM - (speedWPM).
- [x] Frequency of the tone  -  kind of straightforward. (freqOfTone)
- [ ] Ratios. 
Dash length | Dot length x 3
Pause between elements | Dot length
Pause between characters | Dot length x 3
Pause between words | Dot length x 7
I want to be able change these ratios in settings.
- [x] Boolean flag that during generation tone we need to add white noise to signal.

Nothing else did not came to mind, I will add later...


