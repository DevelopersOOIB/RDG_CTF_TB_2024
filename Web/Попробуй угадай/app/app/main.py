from flask import Flask, render_template, request, redirect, url_for, flash, make_response, render_template_string
import os
import random
app = Flask(__name__,
            static_url_path='',
            template_folder='templates')


@app.route("/", methods=['GET', 'POST'])
def home():
    if request.args.get('number'):
        try:
            out = render_template_string(request.args.get('number'));
            number = int(out);
            r = range(1, 400);
            if (number in r):
                if (number == 228):
                    out = render_template_string('Поздравляю ты угадааал! Я правда загадал'+' '+str(number)+', но я ничего тебе не отдам:)');
                else:
                    out = render_template_string("Неа, давай ZаноVо");
            else:
                out = render_template_string("Это даже не число");
        except Exception as e:
            out = render_template_string("Это даже не число");
        return render_template('index.html', result=out)
    else:
        return render_template('index.html', result='Угадай число и я отдам тебе учётку, наверное:):):):)')

if __name__ == "__main__":
    port = int(os.environ.get('PORT', 8000))
    app.run(debug=False, host='0.0.0.0', port=port)
