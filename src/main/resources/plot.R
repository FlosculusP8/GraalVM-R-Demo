function(input) {
svg()

x<- seq(-pi,pi,0.1)
print(plot(x,sin((x))))

svg.off()
}