import { getSkin } from '@/utils/auth'
export const setSkin = function () {
    setTimeout(() => {
        let skin = getSkin()
        if (skin == null) {
            skin = "shallow"
        }
        let link = document.createElement("link");
        link.type = "text/css";
        link.id = "theme";
        link.rel = "stylesheet";
        link.href = `./assets/${skin}.css`;
        document.getElementsByTagName("head")[0].appendChild(link);
    }, 100);
}