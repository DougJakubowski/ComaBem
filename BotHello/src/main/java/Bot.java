import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

public class Bot {

	public static void main(String[] args) throws InterruptedException {

		// Criacao do objeto bot com as informacoes de acesso
		TelegramBot bot = TelegramBotAdapter.build("692610213:AAFRKvbct3L0Dq2FHIHN66chL7OUEgq8tKY");

		// objeto responsavel por receber as mensagens
		GetUpdatesResponse updatesResponse;
		// objeto responsavel por gerenciar o envio de respostas
		SendResponse sendResponse = null;
		// objeto responsavel por gerenciar o envio de aï¿½ï¿½es do chat
		BaseResponse baseResponse;

		// controle de off-set. A partir deste ID serao lidas as mensagens
		// pendentes na fila
		int m = 0;
		int pizza1=0, pizza2=0;
		// loop infinito pode ser alterado por algum timer de intervalo curto
		while (true) {

			// executa comando no Telegram para obter as mensagens pendentes a partir de um
			// off-set (limite inicial)
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));

			// lista de mensagens
			List<Update> updates = updatesResponse.updates();

			// anï¿½lise de cada acao da mensagem
			for (Update update : updates) {

				// atualizacao do off-set
				m = update.updateId() + 1;

				if (update.callbackQuery() != null) {

					sendResponse = bot.execute(new SendMessage(update.callbackQuery().message().chat().id(),
							update.callbackQuery().data()));
				} else {
					System.out.println("Recebendo mensagem:" + update.message().text());

					// envio de "Escrevendo" antes de enviar a resposta
					baseResponse = bot
							.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					// verificacao de acao de chat foi enviada com sucesso
					System.out.println("Resposta de Chat Action Enviada?" + baseResponse.isOk());

					// enviando numero de contato recebido
					if (update.message().contact() != null) {
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
								"NÃºmero " + update.message().contact().phoneNumber() + " enviado"));
					} else if (update.message().location() != null) {
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
								"Latitude " + update.message().location().latitude() + " enviada"));
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Deseja iniciar seu pedido?").replyMarkup(new ReplyKeyboardMarkup
								(new String[] { "Iniciar pedido", "Não"})));
					}

					if (update.message().text() != null) {
						// CriaÃ§Ã£o de Keyboard
						switch(update.message().text()) {
						case "/start":{
							//sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Bem vindo à pizzaria ComaBem").replyMarkup(new ReplyKeyboardMarkup
						//			(new String[] {"Iniciar pedido"})));
							sendResponse = bot
									.execute(new SendMessage(update.message().chat().id(), "Bem vindo à pizzaria ComaBem\nPor favor, envie sua localização")
											.replyMarkup(new ReplyKeyboardMarkup(new KeyboardButton[] { new KeyboardButton("Fornecer localização").requestLocation(true) })));

							break;
						}
						case "Iniciar pedido":{
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Quantas pizzas gostaria?").replyMarkup(new ReplyKeyboardMarkup
									(new String[] { "Uma", "Duas"})));
							
							break;
						}
						case "Uma":{
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Qual sabor?").replyMarkup(new ReplyKeyboardMarkup
									(new String[] { "Mussarela", "Calabresa"},
									new String[] {"Portuguesa", "Italiana"})));
							break;
						}
						case "Mussarela":{
							pizza1=1;
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Pedido registrado com sucesso"));
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "").replyMarkup(new ReplyKeyboardRemove()));
							break;
						}
						case "Calabresa":{
							pizza1=2;
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Pedido registrado com sucesso"));
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "").replyMarkup(new ReplyKeyboardRemove()));
							break;
						}
						case "Portuguesa":{
							pizza1=3;
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Pedido registrado com sucesso"));
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "").replyMarkup(new ReplyKeyboardRemove()));
							break;
						}
						case "Italiana":{
							pizza1=4;
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Pedido registrado com sucesso"));
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "").replyMarkup(new ReplyKeyboardRemove()));
							break;
						}
						case "Duas":{
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Quais sabores?").replyMarkup(new ReplyKeyboardMarkup
									(new String[] { "Mussarela(primeira)", "Calabresa(primeira)"},
									new String[] {"Portuguesa(primeira)", "Italiana(primeira)"})));
							break;
						}
						case "Mussarela(primeira)":{
							pizza1=1;
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "E a outra?").replyMarkup(new ReplyKeyboardMarkup
									(new String[] { "Mussarela(segunda)", "Calabresa(segunda)"},
									new String[] {"Portuguesa(segunda)", "Italiana(segunda)"})));
							break;
						}
						case "Portuguesa(primeira)":{
							pizza1=3;
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "E a outra?").replyMarkup(new ReplyKeyboardMarkup
									(new String[] { "Mussarela(segunda)", "Calabresa(segunda)"},
									new String[] {"Portuguesa(segunda)", "Italiana(segunda)"})));
							break;
						}
						case "Calabresa(primeira)":{
							pizza1=2;
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "E a outra?").replyMarkup(new ReplyKeyboardMarkup
									(new String[] { "Mussarela(segunda)", "Calabresa(segunda)"},
									new String[] {"Portuguesa(segunda)", "Italiana(segunda)"})));
							break;
						}
						case "Italiana(primeira)":{
							pizza1=4;
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "E a outra?").replyMarkup(new ReplyKeyboardMarkup
									(new String[] { "Mussarela(segunda)", "Calabresa(segunda)"},
									new String[] {"Portuguesa(segunda)", "Italiana(segunda)"})));
							break;
						}
						case "Mussarela(segunda)":{
							pizza2=1;
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Pedido registrado com sucesso"));
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "").replyMarkup(new ReplyKeyboardRemove()));
							break;
						}
						case "Calabresa(segunda)":{
							pizza2=2;
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Pedido registrado com sucesso"));
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "").replyMarkup(new ReplyKeyboardRemove()));
							break;
						}
						case "Portuguesa(segunda)":{
							pizza2=3;
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Pedido registrado com sucesso"));
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "").replyMarkup(new ReplyKeyboardRemove()));
							break;
						}
						case "Italiana(segunda)":{
							pizza2=4;
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Pedido registrado com sucesso"));
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "").replyMarkup(new ReplyKeyboardRemove()));
							break;
						}
						case "Não":{
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"A pizzaria ComaBem agradece pela preferência"));
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "").replyMarkup(new ReplyKeyboardRemove()));
							break;
						}
						case "Pedido":{
							if(pizza2==0) {
								if(pizza1==1) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"1 pizza Mussarela"));
								else if(pizza1==2) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"1 pizza Calabresa"));
								else if(pizza1==3) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"1 pizza Portuguesa"));
								else if(pizza1==4) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"1 pizza Italiana"));
								else sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Erro, nenhum pedido encontrado."));
							}
							if(pizza2==1) {
								if(pizza1==1) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"2 pizzas Mussarela"));
								else if(pizza1==2) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"1 pizza Mussarela e 1 pizza Calabresa"));
								else if(pizza1==3) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"1 pizza Mussarela e 1 pizza Portuguesa"));
								else if(pizza1==4) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"1 pizza Mussarela e 1 pizza Italiana"));
								else sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Erro, nenhum pedido encontrado."));
							}
							if(pizza2==2) {
								if(pizza1==1) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"1 pizza Mussarela e 1 pizza Calabresa"));
								else if(pizza1==2) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"2 pizzas Calabresa"));
								else if(pizza1==3) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"1 pizza Portuguesa e 1 pizza Calabresa"));
								else if(pizza1==4) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"1 pizza Italiana e 1 pizza Calabresa"));
								else sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Erro, nenhum pedido encontrado."));
							}
							if(pizza2==3) {
								if(pizza1==1) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"1 pizza Mussarela e 1 pizza Portuguesa"));
								else if(pizza1==2) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"1 pizza Calabresa e 1 pizza Portuguesa"));
								else if(pizza1==3) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"2 pizzas Portuguesa"));
								else if(pizza1==4) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"1 pizza Italiana e 1 pizza Portuguesa"));
								else sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Erro, nenhum pedido encontrado."));
							}
							if(pizza2==4) {
								if(pizza1==1) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"1 pizza Mussarela e 1 pizza Italiana"));
								else if(pizza1==2) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"1 pizza Calabresa e 1 pizza Italiana"));
								else if(pizza1==3) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"1 pizza Portuguesa e 1 pizza Italiana"));
								else if(pizza1==4) sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"2 pizzas Italiana"));
								else sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Erro, nenhum pedido encontrado."));
							}
							break;
						}
						default:{
							pizza1=0;
							pizza2=0;
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Desculpe, não entendi, gostaria de iniciar seu pedido?").replyMarkup(new ReplyKeyboardMarkup
									(new String[] { "Iniciar pedido", "Não"})));
						}
						}
					}

				}

			}

		}

	}

}