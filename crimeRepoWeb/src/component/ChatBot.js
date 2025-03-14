import React, { Fragment, useContext, useState } from "react";
import "../styles/ChatBot.scss";
import Swal from "sweetalert2";
import { CRIMEREPOBOT, STATUSMESSAGES } from "../utils/Messages";
import AuthContext from "../store/AuthContext";

const ChatBot = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);
  const context = useContext(AuthContext);

  let reportsForChatbot = JSON.stringify(context?.crimeData);

  const toggleChat = () => {
    setIsOpen(!isOpen);
  };

  const sendMessage = () => {
    if (input.trim() !== "") {
      const response = CRIMEREPOBOT.FASTACCESSFAQS.find((item) =>
        item.question.toLowerCase().includes(input.toLowerCase())
      );
      if (response) {
        setMessages([...messages, { input, response: response.answer }]);
      } else {
        setLoading(true);
        try {
          window.puter.ai
            .chat(
              CRIMEREPOBOT.PREPROMPT +
                reportsForChatbot +
                CRIMEREPOBOT.USERQUESTION +
                input
            )
            .then((res) => {
              setMessages([
                ...messages,
                { input, response: res.message?.content },
              ]);
            })
            .catch((err) => {
              Swal.fire(
                "Network Error",
                "Please try again later.",
                STATUSMESSAGES.error
              );
            })
            .finally(() => setLoading(false));
        } catch (error) {
          setLoading(false);
          Swal.fire(
            STATUSMESSAGES.ERROR,
            STATUSMESSAGES.WENTWRONG,
            STATUSMESSAGES.error
          );
        }
      }
      setInput("");
    }
  };

  return (
    <div>
      <div className="chatbot-icon fs-2 " onClick={toggleChat}>
        💬
      </div>
      {isOpen && (
        <Fragment>
          <div className="modal-overlay">
            <div className="chat-window">
              <div className="chat-header">
                <span>Chat with us</span>
                <button onClick={toggleChat}>X</button>
              </div>
              <div className="chat-body">
                <div className="w-100 d-flex align-items-center flex-column">
                  <img
                    src="/crimeReportingSystemLogo1.png"
                    alt="crimeRepo-logo"
                    className="rounded p-2 rounded-circle w-25 "
                  />
                  <h4>need help?</h4>
                  <p>
                    <strong>Chat with CrimeRepo Assistant Lustitia</strong>
                  </p>
                </div>
                {messages.map((msg, index) => (
                  <div key={index} className="text-start">
                    <strong>You:</strong> {msg.input}
                    <br />
                    <strong>crimeRepo Bot:</strong> {msg.response}
                  </div>
                ))}

                {loading && (
                  <div className="loader slide ">
                    <span className="loader__dot slide__one"></span>
                    <span className="loader__dot"></span>
                    <span className="loader__dot"></span>
                    <span className="loader__dot slide__two"></span>
                  </div>
                )}
              </div>
              <div className="chat-footer">
                <input
                  type="text"
                  value={input}
                  onChange={(e) => setInput(e.target.value)}
                  placeholder="Type your message..."
                />
                <button onClick={sendMessage}>Send</button>
              </div>
            </div>
          </div>
        </Fragment>
      )}
    </div>
  );
};

export default ChatBot;
