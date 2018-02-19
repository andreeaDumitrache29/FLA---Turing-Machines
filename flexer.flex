import java.util.*;
%%

%class Flexer
%unicode
/*%debug*/
%int
%line
%column

%{

    Alphabet alphabet = new Alphabet();
    ArrayList <TuringMachine> machines = new ArrayList<TuringMachine>();
    ArrayList <ArrayList <Action>> actions = new ArrayList<ArrayList<Action>>();
    ArrayList <String> symbols = new ArrayList<String>();
    Stack <Context> context = new Stack<>();

    Alphabet getAlphabet() {
        return alphabet;
    }

    ArrayList<TuringMachine> getMachines(){
        return machines;
    }
    String name = "";
    String copy;
    TuringMachine m;
    Label l;
    Call c;
    Loop loop;
    Transition trans;
    int ok = 0;
    int crt_idx = -1;

%}

LineTerminator = \r|\n|\r\n
WS = {LineTerminator} | [ \t\f]
Spaces = [ \t\f]
special =  "!"|"#"|"$"|"%"|"&"|"-"|"."|"/"|":"|"<"|">"|"="|"@"|"^"|"`"|"~"|"*"|"?"|"+"
lower = [a-z]
upper = [A-Z]

Symbol = {lower} | [:digit:] | {special} | {upper}
Name = {lower} | [:digit:] | {upper} | "_"


%state CHARA DOTS COMMENT SEP1 SEP2 GENERAL MACHINE_NAME MACHINE CALL TRANS LABEL LOOP
/* States:
   **SEP1, 2: reads the separator inside the alphabet
   **CHARA: reads a symbol(character) of the alphabet
   **DOTS: reads the "::" part of declarations
   **GENERAL: check if a comment or a TM declaration is up next
   **MACHINE_NAME: begin reading the name of a TM
   **MACHINE: check what type of action si about ot begin / end
   **CALL: read the TM being called
   **TRANS: read the transition executed by the machine
   **LABEL: reads a label
   **LOOP: reads the label to which to go back to
*/

%%

<YYINITIAL>"alphabet"	{
    yybegin(SEP1);
}

<SEP1>{WS}  {
   yybegin(DOTS);
}

<DOTS>"::"  {
    yybegin(SEP2);
}

<SEP2>  {
    {WS}    {yybegin(CHARA);}
}

<CHARA> 	{
    {Symbol}    {
        String symbol = yytext();
        alphabet.addSymbol(symbol.charAt(0));
        yybegin(SEP2);
    }

    ";"     {yybegin(GENERAL);}
}

<GENERAL>   {

    {WS}    {}
    ";"     {yybegin(COMMENT);}
    {Name}      
    {
        yypushback(1);
        yybegin(MACHINE_NAME);
    }

}

<COMMENT>   {
    {Symbol}    {}
    {LineTerminator}    {yybegin(GENERAL);}
   . {}
}

<MACHINE_NAME>  {
    {Name}    
    {
        String symbol = yytext();
        name += symbol.charAt(0);
    }

    {WS} {
    copy = name;
    yybegin(MACHINE);}
}

<MACHINE>   {
    "::="   
    {
        m = new TuringMachine(name);
        context.push(m);
        machines.add(m);
        name = "";
    }

    {WS}    {}
    "["     {yybegin(CALL);}
   
    "("     
    {
        actions.add(new ArrayList<Action>());
        trans = new Transition();
        Context cont = context.peek();
        if(cont instanceof TuringMachine || cont instanceof Label){
            cont.addAction(trans);
        }else{
            actions.get(crt_idx).add(trans);
        }
        crt_idx = crt_idx + 1;
        context.add(trans);
        yybegin(TRANS);

    }

    "{"     {yypushback(1); yybegin(TRANS);}
   
    {Name}    
    {
        yypushback(1);
        yybegin(LABEL);
    }
   
    "&"     {yybegin(LOOP);}

    ";;"
    {
        Context cont = context.pop();
        while(true){
            if(cont instanceof TuringMachine){
                break;
            }
            cont = context.pop();
        }
        yybegin(GENERAL);
    }

    ";"
    {
        Context cont = context.peek();
        if(cont instanceof Transition){
            ((Transition)cont).addAction(actions.get(crt_idx));
            actions.get(crt_idx).clear();
        }
    }

    ")"
    {   
        context.pop();
        crt_idx = crt_idx - 1;
        Context cont = context.peek();
        if(cont instanceof Label){
            context.pop ();
        }
    }
}

<CALL>  {
    {Symbol}    
    {
        String symbol = yytext();
        name += symbol.charAt(0);
    }
   
    "("
    {
        m = new TuringMachine(name);
        name = "";
    }
  
    ")"
    {   
        ok = 1;
        c = new Call(name, m);
        name = "";
    }
   
    "]" 
    {
        if(ok == 0){
            m = new TuringMachine(name);
            name = "";
            c = new Call(m);
        }

        Context cont = context.peek();
        if(cont instanceof TuringMachine || cont instanceof Label){
            cont.addAction(c);
        }else{
            actions.get(crt_idx).add(c);
        }
       
        ok = 0;
        yybegin(MACHINE);
    }
}

<TRANS> {
    "{"      {}

    "}"      {}

    {WS}     {}

    {Symbol}
    {
        String symbol = yytext();
        symbols.add(yytext());
    }

    ","     {}

    "->"
    {
        Context cont = context.peek();
        ((Transition)cont).addSymbols(symbols);
        symbols.clear();
        yybegin(MACHINE);
    }

}

<LOOP>  {
    {Name}    
    {
        String symbol = yytext();
        name += symbol.charAt(0);
    }
    {WS}
    {
        loop = new Loop(name);
        Context cont = context.peek();
       
        if(cont instanceof TuringMachine || cont instanceof Label){
            cont.addAction(loop);
        }else{
            actions.get(crt_idx).add(loop);
        }
        
        name = "";
        yybegin(MACHINE);
    } 
}

<LABEL> {
     {Name}    
    {
        String symbol = yytext();
        name += symbol.charAt(0);
    }
   
    "@"     
    {
        l = new Label(name);
        name = "";
        Context cont = context.peek();
        
        if(cont instanceof TuringMachine || cont instanceof Label){
            cont.addAction(l);
        }else{
            actions.get(crt_idx).add(l);
        }
        
        context.push(l);
        yybegin(MACHINE);
    }
}