
a = 10615472175367464266663776781861064380377038775564478974398954759537470086616681357260638842872730010739021894358401376288799807729978341671532133325197637715139207884111626404330430831220218342606575898634439446113084419487139556495664715341900040962532198548057200811876135966282701528755421191716010074693392071266422618898234917893387982662417881137772130952777443034787336499105151370442263505233949077650888970221494578074630501677776221280587002129629976045680695381084383370478914294557350117724623150361935068288605859514397879320929506842881240588851405620002476076176672160117388932468883974754447565038803
b = 2703732538373467503277310307325340920969134839186915995121732610419767351126699218855164583297446395199144532640973426477807452846468572507227216677899664503907156370068465581741076611652223764611581214873533665646395416332936547359252536770335387013429009247825361710016545229301833316426038757537655046377769767830149941784147728180387312897884002736281641547033884779984908143947389123942012113215528979010377216699318430771285337038494658360235351710787252103372956377963725086444642248750886172437905926386431342515384284900874447290898547670944312173661109207495286889027811303604354387806362047265903872735816
z = 19751117038857258768591783235396264847804055214184629237989947740406777683521603825473903403734884668215239071630259189170035535227573326733651001862737899861194602864261914338439966819655526865124191762247964775181992433565364419075092107671076605963465279909429533219917653196167208676561906503331554653060235272474669138325567484882442371904202372803287787127510008304522937409364368280068730969635521014580406183588451359647118401648680333526661765307496963540749058592309858075382632167370984845375617146158233987907771952127887424503125308421000992943589104489669569842490943570415001425184760166309113621673603
p = 19895384145446453205458675340317152402981819991753630707835515789342995670957748194083673052224258384874234465952235406028855785174400403009309567351401163592642892011315375430708862507274653063696698466310795167295731320098795296726026079081153093823042268651354892397889384043070265847174856891752224034172735322753331646438003679447653854783049594520486666032569469158069162888147459939390108012382155873766956222391945003418428639557210692572694534736873439727256598509136727204749777015524533757378161985080260787900257308202491715497109490900514107044133777833862007253533225764072181862514388986295451797998529

def main():
    A = Matrix([[1, 0, 0, a], 
                [0, 1, 0, b], 
                [0, 0, 1, z], 
                [0, 0, 0, p]])  
    res = [abs(i) for i in A.LLL()[0]]
    x, y = res[0], res[1]
    len_x = (int(x).bit_length() + 7) // 8
    flagx = int(x).to_bytes(len_x, byteorder = 'big')
    len_y = (int(y).bit_length() + 7) // 8
    flagy = int(y).to_bytes(len_y, byteorder = 'big')
    print(flagx + flagy)

if __name__ == "__main__":
    main()